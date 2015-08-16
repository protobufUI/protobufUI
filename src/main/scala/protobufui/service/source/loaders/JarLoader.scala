package protobufui.service.source.loaders

import java.io.{File, FileInputStream, InputStream}
import java.net.{URI, URL}
import java.util.jar.JarInputStream

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.ClassesLoader.{Put}
import protobufui.service.source.{Load, Utils}

import scala.io.Source

class JarLoader
  extends Actor with ActorLogging {

  val uuid = java.util.UUID.randomUUID toString

  val temporaryStorage: File = new File(System.getProperty("java.io.tmpdir"), "protobuf/" + uuid)

  override def receive: Receive = {
    case Load(file: File) =>
      val fileURL = file.toURI.toString
      val jarInputStream = new JarInputStream(new FileInputStream(file))

    Stream.continually(jarInputStream.getNextEntry).takeWhile(_ != null)
      .map(_.getName)
      .filter(_.endsWith(".java"))
      .map(pathInJar => (new URI("jar:" + fileURL + "!/" + pathInJar).toURL, pathInJar))
      .map((t: (URL, String)) => (t._1.openConnection.getInputStream, t._2))
      .map((t: (InputStream, String)) => (Source.fromInputStream(t._1).getLines().mkString("\n"), t._2))
      .map((t: (String, String)) => {
      val fileWithSources: File = new File(temporaryStorage, t._2)
      Utils.createFileWithContent(fileWithSources, t._1)
    })

    context.parent ! Put(temporaryStorage)
  }
}
