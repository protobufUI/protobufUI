package protobufui.service.mock

import java.net.InetSocketAddress

import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive
import akka.io.Tcp._
import akka.io.{Tcp, IO}


abstract class MockReceiveController(address: InetSocketAddress) extends Actor with ActorLogging {

/*  protected val io = IO(Tcp)

  io ! Bind(self, address)

  override def receive: Receive = {
    case Bound(localAddress) =>
      Console.out.println(s"Actor ${self.path} bound to ${localAddress.toString}")

    case CommandFailed(_: Bind) =>
      Console.out.println("Binding failed")
      context stop self

    case Connected(remote, local) =>
      Console.out.println(s"Successfully connected to $local")
      sender() ! Register(self)
      context become listening
  }*/

}
