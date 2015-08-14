package protobufui.service.source.loaders

import java.io.File

import akka.actor.{Actor, ActorLogging}

case class LoadProto(file: File)

class ProtoLoader
  extends Actor with ActorLogging {
  override def receive: Receive = {
    case LoadProto(file) => ???
  }
}
