package protobufui.service.mock

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp.Write
import akka.util.ByteString
import com.google.protobuf.MessageLite
import protobufui.service.mock.PbMessageResponder.Respond

import scala.reflect._


object PbMessageResponder {
  case class Respond(msg: MessageLite, connection: ActorRef)
}


class PbMessageResponder[Req <: MessageLite : ClassTag, Res <: MessageLite](getResponse: Req => Res) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Respond(msg, connection) if classTag[Req].runtimeClass.isInstance(msg) =>
      connection ! Write(ByteString(getResponse(msg.asInstanceOf[Req]).toByteArray))
  }

}
