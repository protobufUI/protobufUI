package protobufui.service.socket

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.socket.UdpMessageSender._
import test.PbTest.Person

/**
 * Created by humblehound on 16.07.15.
 */
class UdpMessageSenderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("MessageActorSpec"))

  def fixture = new {
    val parent = TestProbe()
    val inetaddr = InetAddress.getByName("jira.krever.co.vu")
    val props = Props(new UdpMessageSender(new InetSocketAddress(inetaddr, 8080)))
    val msgBytes = ByteString(Person.newBuilder().setName("Jan").setEmail("j@k.pl").setId(1).build().toByteArray)
  }

  "UdpMessageSender" should {
    "send UDP message successfully" in {
      //given
      val f = fixture
      val messageSender = TestActorRef(f.props, f.parent.ref, s"$UdpMessageSender")
      f.parent.expectMsg(UdpMessageSenderReady)
      //when
      messageSender ! SendMessage(f.msgBytes)
      //then
      f.parent.expectMsg(UdpMessageSendSuccessfully)
    }

    s"send $UdpMessageSenderNotReady when not bound" in {
      //given
      val f = fixture
      val messageSender = TestActorRef(f.props, f.parent.ref, s"$UdpMessageSender")
      //when
      messageSender ! SendMessage(f.msgBytes)
      //then
      f.parent.expectMsg(UdpMessageSenderNotReady)
    }

    s"send $UdpMessageSenderFinalized when told to finalize" in {
      //given
      val f = fixture
      val messageSender = TestActorRef(f.props, f.parent.ref, s"$UdpMessageSender")
      f.parent.expectMsg(UdpMessageSenderReady)
      //when
      messageSender ! Finalize
      //then
      f.parent.expectMsg(UdpMessageSenderFinalized)
    }
  }
}
