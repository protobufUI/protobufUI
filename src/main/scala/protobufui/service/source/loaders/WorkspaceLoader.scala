package protobufui.service.source.loaders

import java.io.File

import akka.actor.{Actor, ActorLogging}

case class LoadClasses()

class WorkspaceLoader(workspaceRoot: File) extends Actor with ActorLogging {
  override def receive: Receive = {
    case LoadClasses => ???
  }
}
