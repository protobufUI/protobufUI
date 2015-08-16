package protobufui.service.source.loaders

import java.io.{File, FileInputStream}
import java.net.URI
import java.util.jar.JarInputStream

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.ClassesLoader.Put
import protobufui.service.source.{Load, Utils}

import scala.io.Source

class JarLoader extends Actor with ActorLogging {

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
      .map
    { case (jarURL, packageLikePathInJar) =>
      (jarURL.openConnection.getInputStream, packageLikePathInJar)
    }
      .map
    { case (inputStream, packageLikePathInJar) =>
      (Source.fromInputStream(inputStream).getLines().mkString("\n"), packageLikePathInJar)
    }
      .foreach
    { case (sources, packageLikePathInJar) => {
      val fileWithSources: File = new File(temporaryStorage, packageLikePathInJar)
      Utils.createFileWithContent(fileWithSources, sources)
    }
    }

    context.parent ! Put(temporaryStorage)
  }
}
