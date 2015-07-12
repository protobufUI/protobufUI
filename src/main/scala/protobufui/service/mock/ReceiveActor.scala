package protobufui.service.mock

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.actor.Actor.Receive
import akka.io.{IO, Tcp}
import com.google.protobuf
import com.google.protobuf.{TextFormat, Parser}

import scala.util.{Failure, Success, Try}

/**
 * Created by krever on 7/12/15.
 */
class ReceiveActor(expectedMsgClass: Class[_], socketAddress: InetSocketAddress) extends Actor with ActorLogging{

  import Tcp._
  import context.system

  val msgParser: Parser[_ <: protobuf.Message] = expectedMsgClass.getDeclaredMethod("getParserForType", new Array[Class[_]](0):_*).invoke(null, new Array[Object](0)).asInstanceOf

  IO(Tcp) ! Bind(self, socketAddress)

  override def receive: Receive = binding

  def binding: Receive = {
    case b @ Bound(localAddress) =>
      log.debug(s"Actor ${self.path.name} bound to ${localAddress.toString}")

    case CommandFailed(_: Bind) =>
      log.error("Binding failed")
      context stop self

    case c @ Connected(remote, local) =>
      log.debug(s"Successfully connected to $local")
      sender() ! Register(self)
      context become listening
  }

  def listening: Receive = {
    case Received(data) =>
      val msg = Try(msgParser.parseFrom(data.toArray))
      msg match {
        case Success(value) => log.debug(s"Received message: ${TextFormat.printToString(value)}")
        case Failure(e) => log.error(e, "Message could not be parsed")
      }
    case PeerClosed =>
      log.debug("Peer closed")
      context stop self
  }

}
