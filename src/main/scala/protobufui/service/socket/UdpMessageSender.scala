package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import akka.io.{IO, Udp}
import akka.util.ByteString
import protobufui.service.socket.UdpMessageSender._

/**
 * Created by humblehound on 15.07.15.
 */

object UdpMessageSender {
  case class SendMessage(msg: ByteString)
  case class UdpMessageSenderNotReady()
  case class UdpMessageSenderReady()
  case class UdpMessageSendSuccessfully()
  case class Finalize()
  case class UdpMessageSenderFinalized()
}

class UdpMessageSender(_iNetSocketAddress: InetSocketAddress) extends Actor with ActorLogging {

  IO(Udp)(context.system) ! Udp.SimpleSender

  override def receive: Receive = {

    case Udp.SimpleSenderReady => {
      context.become(ready(sender()))
      context.parent ! UdpMessageSenderReady
    }

      def ready(udpSocket: ActorRef): Receive = {

        case SendMessage(message) => {
          udpSocket ! Udp.Send(message, _iNetSocketAddress)
          context.parent ! UdpMessageSendSuccessfully
        }

        case Finalize => {
          IO(Udp)(context.system) ! PoisonPill
          context.parent ! UdpMessageSenderFinalized
        }
      }

    case _ => {
      context.parent ! UdpMessageSenderNotReady
    }
  }
}
