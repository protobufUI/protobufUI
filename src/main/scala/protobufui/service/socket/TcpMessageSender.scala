package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.google.protobuf.MessageLite
import protobufui.service.socket.TcpMessageSender.{SendMessage, TcpMessageSenderReady}

object TcpMessageSender {

  case class SendMessage(message: MessageLite)

  case class TcpMessageSenderReady()

}

class TcpMessageSender(address: InetSocketAddress) extends Actor with ActorLogging {

  import context._

  protected val ioTcp = IO(Tcp)

  override def preStart(): Unit = {
    ioTcp ! Bind(self, address)
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
          ioTcp ! Write(ByteString(message.toByteArray))
        case CommandFailed(w: Write) =>
          log.debug("Message sending failed")
        case _: ConnectionClosed =>
          context stop self
        case _ =>
          log.debug("Invalid message received, ignored");
      }

    case _ =>
      log.debug("Invalid message, MessageSender not connected")
  }

}
