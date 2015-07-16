package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.{IO, Udp}
import akka.util.ByteString
import protobufui.service.socket.PbMessageSender._

/**
 * Created by humblehound on 15.07.15.
 */

object PbMessageSender {
  case class SendMessage(msg: ByteString)
  case class PbMessageSenderReady()
  case class PbMessageSendSuccessfully()
  case class PbMessageSenderNotReady() extends Throwable
}

class PbMessageSender(_iNetSocketAddress : InetSocketAddress) extends Actor with ActorLogging{

  var udpSocket : ActorRef = null

  IO(Udp)(context.system) ! Udp.SimpleSender

  override def receive: Receive = {

    case Udp.SimpleSenderReady =>{
      udpSocket = sender
      context.parent ! PbMessageSenderReady
      Console.out.println("bound ok")
    }

    case SendMessage(message) => {
      if(udpSocket == null){
        throw new PbMessageSenderNotReady()
      }

       udpSocket ! Udp.Send(message, _iNetSocketAddress)
       context.parent ! PbMessageSendSuccessfully()
      Console.out.println("message sent")
    }
  }
}
