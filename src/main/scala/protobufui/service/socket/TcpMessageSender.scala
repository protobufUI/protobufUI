package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor._
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
          log.warning(s"Invalid message received, ignored.Msg: $m");
      }

    case m =>
      log.warning(s"Invalid message, MessageSender not connected. Msg: $m")
      context.stop(self)
  }

}

class MessageSenderSupervisor(address: InetSocketAddress, message: MessageLite, onResponse: ByteString => Unit, onFailure: () => Unit) extends Actor {

  val tcpMessageSender = context.actorOf(Props(new TcpMessageSender(address)))

  context.watch(tcpMessageSender)

  override def receive: Receive = {
    case TcpMessageSenderReady =>
      tcpMessageSender ! SendMessage(message)
    case Response(data) =>
      onResponse(data)
      context.stop(self)
    case Terminated(`tcpMessageSender`) =>
      onFailure()
      context.stop(self)
  }

}
