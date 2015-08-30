package protobufui.service.mock

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp.Write
import akka.util.ByteString
import com.google.protobuf.MessageLite
import protobufui.service.mock.PbMessageResponder.Respond


object PbMessageResponder {
  case class Respond(msg: MessageLite, connection: ActorRef)
}


class PbMessageResponder(getResponse: PartialFunction[MessageLite, MessageLite]) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Respond(msg, connection) if getResponse.isDefinedAt(msg) =>
      connection ! Write(ByteString(getResponse(msg).toByteArray))
  }

}
