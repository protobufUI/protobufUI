package protobufui.service.mock

import akka.actor.{ActorSystem, Props}
import akka.io.Tcp.{PeerClosed, Received}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.mock.PbMessageParser.Parsed
import test.PbTest.Person

class PbMessageParserSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender{

  def this() = this(ActorSystem("ReceiveActorSpec"))
  
    "PbMessageParser" should {
      "parse message and return the result" in {
        //given
        val person: Person = Person.newBuilder().setName("Jan").setEmail("j@k.pl").setId(1).build()
        val parser = TestActorRef(Props(new PbMessageParser[Person](classOf[Person])), self, "Parser1")
        val msg = Received(ByteString(person.toByteArray))
        //when
        parser ! msg
        //then
        expectMsg(Parsed(person))
      }
      "ignore invalid data" in {
        //given
        val parser = TestActorRef(Props(new PbMessageParser[Person](classOf[Person])), self, "Parser2")
        //when
        parser ! Received(ByteString(1,2,3,4,5))
        //then
        expectNoMsg()
      }
      "stop self on PeerClosed" in {
        //given
        val parser = TestActorRef(Props(new PbMessageParser[Person](classOf[Person])), self, "Parser3")
        val probe = TestProbe()
        probe watch parser
        //when
        parser ! PeerClosed
        //then
        probe.expectTerminated(parser)
      }
    }
}
