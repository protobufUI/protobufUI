package protobufui.service.socket

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.{ActorSystem, Props}
import akka.io.Tcp.Received
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.socket.PbMessageSender.{PbMessageSendSuccessfully, SendMessage, PbMessageSenderReady}
import test.PbTest.Person

/**
 * Created by humblehound on 16.07.15.
 */
class PbMessageSenderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("MessageActorSpec"))

  "PbMessageSender" should {
    "send UDP message successfully" in {
      //given
      val inetaddr = InetAddress.getByName("jira.krever.co.vu")
      val messageSender = TestActorRef(Props( new PbMessageSender(new InetSocketAddress(inetaddr, 8080))))
      val person: Person = Person.newBuilder().setName("Jan").setEmail("j@k.pl").setId(1).build()
      val msgBytes = ByteString(person.toByteArray)
      //when
      messageSender ! SendMessage(msgBytes)
      //then
      expectMsg(PbMessageSendSuccessfully)
    }
}
