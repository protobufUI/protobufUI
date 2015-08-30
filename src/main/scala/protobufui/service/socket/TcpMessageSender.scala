package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.google.protobuf.MessageLite
import protobufui.service.socket.TcpMessageSender.{Response, SendMessage, TcpMessageSenderReady}

object TcpMessageSender {

  case class SendMessage(message: MessageLite)

  case class TcpMessageSenderReady()

  case class Response(byteString: ByteString)

}

class TcpMessageSender(address: InetSocketAddress) extends Actor with ActorLogging {

  import context._

  protected val ioTcp = IO(Tcp)

  override def preStart(): Unit = {
    ioTcp ! Connect(address)
  }

  override def receive = {
    case c@Connected(remote: InetSocketAddress, local: InetSocketAddress) => {
      val connection = sender()
      connection ! Register(self)
      context.become(ready(sender()))
      context.parent ! TcpMessageSenderReady
    }
      def ready(tcpSocket: ActorRef): Receive = {
        case SendMessage(message: MessageLite) =>
          tcpSocket ! Write(ByteString(message.toByteArray))
        case CommandFailed(w: Write) =>
          log.warning("Message sending failed")
        case Received(data) =>
          parent ! Response(data)
        case _: ConnectionClosed =>
          context stop self
        case m =>
          log.warning("Invalid message received, ignored.Msg: $m");
      }

    case m =>
      log.warning(s"Invalid message, MessageSender not connected. Msg: $m")
  }

}
