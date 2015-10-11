package protobufui.cli

import java.io.File
import java.time.LocalDateTime
import javax.xml.bind.{JAXBContext, Marshaller}

import akka.actor.Props
import akka.pattern.ask
import ipetoolkit.workspace.WorkspacePersistence
import ipetoolkit.workspace.WorkspacePersistence.{Load, Loaded}
import protobufui.gui.workspace.base.RootEntry
import protobufui.service.source.ClassesLoader
import protobufui.service.source.ClassesLoader.Put
import protobufui.test.format.TestReportCreator
import protobufui.test.format.generated.Testsuites
import protobufui.{Globals, Main}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object CliMain {
  def start(config: Config) = {
    Globals.setProperty(Globals.Keys.workspaceRoot, config.workspace.getParentFile.getAbsolutePath)
    val workspacePersistence = Main.actorSystem.actorOf(Props(classOf[WorkspacePersistence]))

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
    val classesLoader = Main.actorSystem.actorOf(Props(new ClassesLoader(config.workspace.getParentFile)))

    if(config.load!=null) classesLoader ! Put(config.load)

    //Thread.sleep(10000)
    import Main.actorSystem.dispatcher
    val testSuites = if (config.suites.nonEmpty) rootEntry.getTests.filter(s => config.suites.contains(s.nameProperty.getValue)) else rootEntry.getTests

    Console.out.println("Running Tests")
    val suitesResultsFututres = testSuites.map(_.run(Main.actorSystem))
    val suitesResults = Await.result(Future.sequence(suitesResultsFututres), 1 minute)

    Console.out.println("Creating Report")
    val jaxbResult = TestReportCreator.createReport(suitesResults) // todo: dla casow
    val reportsRoot = Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + "reports"

    Console.out.println("Saving Report")
    val reportFile: File = saveReport(jaxbResult, reportsRoot)

    Console.out.println("Report Saved: " + reportFile.getAbsolutePath)
    Main.actorSystem.shutdown()
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
