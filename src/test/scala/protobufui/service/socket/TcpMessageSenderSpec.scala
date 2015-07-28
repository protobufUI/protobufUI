package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import akka.io.Tcp.{Connected, Write}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.socket.TcpMessageSender.{SendMessage, TcpMessageSenderReady}
import test.PbTest.Person

class TcpMessageSenderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("TcpMessageSenderSpec"))

  "TcpMessageSender" should {
    "become ready on bind" in {
      val localAddress = new InetSocketAddress("localhost", 8080)
      val remoteAddress = new InetSocketAddress("localhost2", 8081)
      val ioTcpProbe = TestProbe()
      val parent = TestProbe()
      val connectedMsg = Connected(localAddress, remoteAddress)
      //when
      val messageSender = TestActorRef(Props(new TcpMessageSender(localAddress) {
        override val ioTcp = ioTcpProbe.ref
      }), parent.ref, "TcpMessageSender")
      ioTcpProbe.send(messageSender, connectedMsg)
      //      //then
      parent.expectMsg(TcpMessageSenderReady)
    }

    "send message without error after bind" in {
      val localAddress = new InetSocketAddress("localhost", 8082)
      val remoteAddress = new InetSocketAddress("localhost2", 8083)
      val ioTcpProbe = TestProbe()
      val parent = TestProbe()
      val msg = Person.newBuilder().setName("Jan").setEmail("j@k.pl").setId(1).build()
      val connectedMsg = Connected(localAddress, remoteAddress)
      //when
      val messageSender = TestActorRef(Props(new TcpMessageSender(localAddress) {
        override val ioTcp = ioTcpProbe.ref
      }), parent.ref, "TcpMessageSender")
      ioTcpProbe.send(messageSender, connectedMsg)
      parent.expectMsg(TcpMessageSenderReady)
      messageSender ! SendMessage(msg)
      //then
      ioTcpProbe.receiveN(2) // ignore bind and register
      ioTcpProbe.expectMsg(Write(ByteString(msg.toByteArray)))
    }
  }

}
