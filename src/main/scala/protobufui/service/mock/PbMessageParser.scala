package protobufui.service.mock

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.actor.Actor.Receive
import akka.io.{IO, Tcp}
import com.google.protobuf
import com.google.protobuf.{MessageOrBuilder, Message, TextFormat, Parser}
import protobufui.service.mock.PbMessageParser.Parsed
import test.PbTest.Person

import scala.util.{Failure, Success, Try}


object PbMessageParser {
  case class Parsed(msg: Message)
}

class PbMessageParser[T <: Message](expectedMsgClass: Class[T]) extends Actor with ActorLogging{

  import Tcp._
  import context.system

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
