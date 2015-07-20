package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import protobufui.service.socket.TcpBond.{BindingFailed, PeerConnected}

object TcpBond {

  case class PeerConnected(connection: ActorRef, connectedMsg: Connected)

  case class BindingFailed(address: InetSocketAddress)

}

class TcpBond(address: InetSocketAddress) extends Actor with ActorLogging {

  import context._

  protected val ioTcp = IO(Tcp)

  override def preStart(): Unit = {
    ioTcp ! Bind(self, address)
  }

  override def receive: Receive = {
    case Bound(localAddress) =>
      log.debug(s"Actor ${self.path} bound to $localAddress")

    case CommandFailed(_: Bind) =>
      log.debug("Binding failed")
      context.parent ! BindingFailed(address)
      context stop self

    case c@Connected(remote, local) =>
      context.parent ! PeerConnected(sender(), c)
  }

}
