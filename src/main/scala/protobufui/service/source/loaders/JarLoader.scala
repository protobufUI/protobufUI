package protobufui.service.source.loaders

import java.io.{File, FileInputStream, InputStream}
import java.net.{URI, URL}
import java.util.jar.JarInputStream

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.{PutJavaFilesFromDirectory, Utils}

import scala.io.Source

case class LoadJar(file: File)

class JarLoader
  extends Actor with ActorLogging {

  val workspaceStorage: File = new File(System.getProperty("java.io.tmpdir"), "protobuf/" + uuid)

  def uuid = java.util.UUID.randomUUID.toString

  override def receive: Receive = {
    case LoadJar(file: File) => ???
  }

  def saveJarAsSources(jarFile: File): Stream[(File, String)] = {
    val fileURL = jarFile.toURI.toString
    val jarInputStream = new JarInputStream(new FileInputStream(jarFile))
    Stream.continually(jarInputStream.getNextEntry).takeWhile(_ != null)
      .map(_.getName)
      .filter(_.endsWith(".java"))
      .map(pathInJar => (new URI("jar:" + fileURL + "!/" + pathInJar).toURL, pathInJar))
      .map((t: (URL, String)) => (t._1.openConnection.getInputStream, t._2))
      .map((t: (InputStream, String)) => (Source.fromInputStream(t._1).getLines().mkString("\n"), t._2))
      .map((t: (String, String)) => {
      // how to make (x:String,y:String) to not use _1 and _2
      val fileWithSources: File = new File(workspaceStorage, t._2) // might not work: then change into ... +"/"+ ...
      Utils.createFileWithContent(fileWithSources, t._1)
    })

    context.parent ! PutJavaFilesFromDirectory(workspaceStorage)
  }
}
