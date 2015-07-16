package protobufui.service.mock

import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp
import com.google.protobuf.{Message, Parser}
import protobufui.service.mock.PbMessageParser.Parsed

import scala.util.{Failure, Success, Try}


object PbMessageParser {
  case class Parsed(msg: Message)
}

class PbMessageParser[T <: Message](expectedMsgClass: Class[T]) extends Actor with ActorLogging{

  import Tcp._

  val msgParser: Parser[T] = expectedMsgClass.getField("PARSER").get(null).asInstanceOf[Parser[T]]

  def receive = {
    case Received(data) =>
      val msg = Try(msgParser.parseFrom(data.toArray))
      msg match {
        case Success(value) => context.parent ! Parsed(value)
        case Failure(e) => log.error(e, "Message could not be parsed.")
      }
    case PeerClosed =>
      context stop self
  }

}
