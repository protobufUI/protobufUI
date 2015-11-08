package protobufui.cli

import java.io.File
import java.time.LocalDateTime
import javax.xml.bind.{JAXBContext, Marshaller}

import akka.actor.{ActorSystem, Props}
import ipetoolkit.workspace.WorkspacePersistence
import ipetoolkit.workspace.WorkspacePersistence.{Load, Loaded}
import protobufui.model.RootEntry
import protobufui.service.message.ClassesLoader
import protobufui.service.message.ClassesLoader.Put
import protobufui.service.testengine.report.TestReportCreator
import protobufui.service.testengine.report.generated.Testsuites
import protobufui.service.testengine.runner.TestSuiteRunner
import protobufui.util.Globals

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import akka.pattern.ask

object CliMain {

  implicit val actorSystem = ActorSystem("protbufui-cli")

  def start(config: Config) = {
    Globals.setProperty(Globals.Keys.workspaceRoot, config.workspace.getParentFile.getAbsolutePath)
    val workspacePersistence = actorSystem.actorOf(Props(classOf[WorkspacePersistence]))

    implicit val timeout = akka.util.Timeout(30 second)

    val loadedMsg = Await.result((workspacePersistence ? Load(config.workspace)).mapTo[Loaded], 30 seconds)
    loadedMsg match {
      case Loaded(Success(rootEntry: RootEntry)) =>
        runTests(config, rootEntry)
      case Loaded(Failure(e)) =>
        Console.out.println("Workspace could not be parsed.")
        e.printStackTrace()
    }


  }

  def runTests(config: Config, rootEntry: RootEntry) = {


    implicit val timeout = akka.util.Timeout(30 second)
    val classesLoader = actorSystem.actorOf(Props(new ClassesLoader(config.workspace.getParentFile)))

    if(config.load!=null) classesLoader ! Put(config.load)

    //Thread.sleep(10000)
    val testSuites = if (config.suites.nonEmpty) rootEntry.getTests.filter(s => config.suites.contains(s.getName)) else rootEntry.getTests

    Console.out.println("Running Tests")
    val suitesResultsFututres = testSuites.map(new TestSuiteRunner(_).run(actorSystem))
    import actorSystem.dispatcher
    val suitesResults = Await.result(Future.sequence(suitesResultsFututres), 1 minute)

    Console.out.println("Creating Report")
    val jaxbResult = TestReportCreator.createReport(suitesResults) // todo: dla casow
    val reportsRoot = Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + "reports"

    Console.out.println("Saving Report")
    val reportFile: File = saveReport(jaxbResult, reportsRoot)

    Console.out.println("Report Saved: " + reportFile.getAbsolutePath)
    actorSystem.shutdown()
    System.exit(0)
  }

  def saveReport(jaxbResult: Testsuites, reportsRoot: String): File = {
    val jaxbContext = JAXBContext.newInstance(classOf[Testsuites])
    val jaxbMarshaller = jaxbContext.createMarshaller()
    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
    val reportFile: File = new File(reportsRoot, LocalDateTime.now.toString.replace(":", "_").replace(".", "_") + ".xml")
    reportFile.getParentFile.mkdirs()
    jaxbMarshaller.marshal(jaxbResult, reportFile)
    reportFile
  }
}

//TODO nieserializuje sie messages
