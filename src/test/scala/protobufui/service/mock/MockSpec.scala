package protobufui.service.mock

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.io.Tcp.Connected
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.mock.PbMessageParser.Parsed
import protobufui.service.mock.PbMessageResponder.Respond
import protobufui.service.socket.TcpBond.PeerConnected
import test.PbTest.Person

class MockSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("ReceiveActorSpec"))

  val mockDefinition = MockDefinition(new InetSocketAddress("localhost", 12345), classOf[Person], (p: Person) => Person.getDefaultInstance)

  "Mock" should {
    "spawn TcpBond and Responder on start" in {
      //given
      //when
      val mock = TestActorRef(new Mock[Person, Person](mockDefinition))
      //then
      mock.children.size should be(2)
    }

    "spawn new parser when peer has conected" in {
      //given
      val tcpBondProbe = TestProbe()
      val connection1 = TestProbe()
      val connection2 = TestProbe()
      val address = new InetSocketAddress("localhost", 12346)
      val mock = TestActorRef(new Mock[Person, Person](mockDefinition))
      //when
      tcpBondProbe.send(mock, PeerConnected(connection1.ref, Connected(address, address)))
      tcpBondProbe.send(mock, PeerConnected(connection2.ref, Connected(address, address)))
      //then
      mock.children.size should be(4)
    }

    "forward parsed message to responder" in {
      //given
      val responderProbe = TestProbe()
      val mock = TestActorRef(new Mock[Person, Person](mockDefinition) {
        override val responder = responderProbe.ref
      })
      val parser = TestProbe()
      val connection = TestProbe()
      val msg = Person.getDefaultInstance
      //when
      parser.send(mock, Parsed(msg, connection.ref))
      //then
      responderProbe.expectMsg(Respond(msg, connection.ref))
    }
  }

}