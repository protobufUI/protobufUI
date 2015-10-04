package protobufui.cli

import java.io.File
import java.time.LocalDateTime
import javax.xml.bind.{Marshaller, JAXBContext}

import akka.actor.Props
import ipetoolkit.workspace.WorkspaceManagement.LoadWorkspace
import ipetoolkit.workspace.WorkspacePersistence.{Load, Loaded}
import ipetoolkit.workspace.{WorkspacePersistence, WorkspaceManager}
import protobufui.gui.workspace.base.RootEntry
import protobufui.service.source.ClassesLoader.Put
import protobufui.test.format.TestReportCreator
import protobufui.test.format.generated.Testsuites
import protobufui.test.{TestCaseResult, TestSuiteResult}
import protobufui.{Globals, Main}
import protobufui.service.source.ClassesLoader

import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object CliMain {
  def start(config:Config)={
    Globals.setProperty(Globals.Keys.workspaceRoot,config.workspace.getParentFile.getAbsolutePath)
    val workspacePersistence = Main.actorSystem.actorOf(Props(classOf[WorkspacePersistence]))

    implicit val timeout = akka.util.Timeout(30 second)

    val loadedMsg = Await.result((workspacePersistence ? Load(config.workspace)).mapTo[Loaded], 30 seconds)
    loadedMsg match {
      case Loaded(Success(rootEntry:RootEntry)) =>
        runTests(config, rootEntry)
      case Loaded(Failure(e)) =>
        Console.out.println("Workspace could not be parsed.")
        e.printStackTrace()
    }


  }

  def runTests(config: Config, rootEntry: RootEntry) = {


    implicit val timeout = akka.util.Timeout(30 second)
    val classesLoader = Main.actorSystem.actorOf(Props(new ClassesLoader(config.workspace.getParentFile)))

    classesLoader ! Put(config.load)

    Thread.sleep(10000)
    val suitesRunner = Main.actorSystem.actorOf(Props(new SuitesRunner(config.suites, rootEntry)))
    val casesRunner = Main.actorSystem.actorOf(Props(new CasesRunner(config.cases, rootEntry)))
    val suitesResultsFuture = (suitesRunner ? RunSuites).mapTo[List[TestSuiteResult]]
    val casesResultsFuture = (casesRunner ? RunCases).mapTo[List[TestCaseResult]]
    val suitesResults = Await.result(suitesResultsFuture,30 seconds)
    //val casesResults = Await.result(casesResultsFuture,30 seconds)
    val jaxbResult = TestReportCreator.createReport(suitesResults) // todo: dla cas�w


    val reportsRoot = Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + "reports"

    val jaxbContext = JAXBContext.newInstance(classOf[Testsuites])
    val jaxbMarshaller = jaxbContext.createMarshaller()
    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
    val reportFile: File = new File(reportsRoot, LocalDateTime.now.toString.replace(":","_").replace(".","_")+".xml")
    reportFile.getParentFile.mkdirs()
    jaxbMarshaller.marshal(jaxbResult, reportFile)
  }
}
