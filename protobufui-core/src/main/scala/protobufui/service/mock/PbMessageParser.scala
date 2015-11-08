package protobufui.service.mock

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp
import com.google.protobuf.{Parser, MessageLite}
import protobufui.model.{MessageLiteClass, MessageClass}
import protobufui.service.mock.PbMessageParser.Parsed

import scala.util.{Failure, Success, Try}


object PbMessageParser {

  case class Parsed(msg: MessageLite, fromConnection: ActorRef)
}

//TODO przerobic na MessageClass
class PbMessageParser(expectedMsgClass: MessageLiteClass, connection: ActorRef) extends Actor with ActorLogging {

  import Tcp._

  val msgParser: Parser[_] = expectedMsgClass.getParser

  connection ! Register(self)

  def receive = {
    case Received(data) =>
      val msg = Try(msgParser.parseFrom(data.toArray))
      msg match {
        case Success(value) => context.parent ! Parsed(value.asInstanceOf[MessageLite], connection)
        case Failure(e) => log.error(e, "Message could not be parsed.")
      }
    case PeerClosed =>
      context stop self
  }

}
