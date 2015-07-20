package protobufui.service.source

import java.io.File

import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp.PeerClosed
import protobufui.service.source.JarLoader.Load

import scala.util.{Failure, Success}

/**
 * Created by pcejrowski on 2015-07-16.
 */
object JarLoader {
    case class Load(file: File)
}

  class JarLoader() extends Actor with ActorLogging{

    def receive = {
      case Load(file) =>
        val loadedJar = ??? //load jar(file)
        loadedJar match {
          case Success(value) => context.parent ! JarReference()
          case Failure(e) => log.error(e, "Java Archive could not be loaded.")
        }
      case PeerClosed =>
        context stop self
    }
  }
