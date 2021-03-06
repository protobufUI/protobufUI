package protobufui.service.mock

import akka.actor.{ActorSystem, Props}
import akka.io.Tcp.{PeerClosed, Received, Register}
import akka.testkit.{TestActorRef, TestProbe, ImplicitSender, TestKit}
import akka.util.ByteString
import org.scalatest.{WordSpecLike, Matchers}
import protobufui.model.MessageClass
import protobufui.service.mock.PbMessageParser.Parsed
import test.PbTest.Person

class PbMessageParserSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender{

  def this() = this(ActorSystem("ReceiveActorSpec"))
  
    "PbMessageParser" should {

      "register self to given connection on start" in {
        //give
        val connection = TestProbe()
        //when
        val parser = TestActorRef(Props(new PbMessageParser(MessageClass(classOf[Person]), connection.ref)), self, "Parser1")
        //then
        connection.expectMsg(Register(parser))
      }

      "parse message and return the result" in {
        //given
        val person: Person = Person.newBuilder().setName("Jan").setEmail("j@k.pl").setId(1).build()
        val connection = TestProbe()
        val parser = TestActorRef(Props(new PbMessageParser(MessageClass(classOf[Person]), connection.ref)), self, "Parser2")
        val msg = Received(ByteString(person.toByteArray))
        //when
        parser ! msg
        //then
        expectMsg(Parsed(person, connection.ref))
      }
      "ignore invalid data" in {
        //given
        val connection = TestProbe()
        val parser = TestActorRef(Props(new PbMessageParser(MessageClass(classOf[Person]), connection.ref)), self, "Parser3")
        //when
        parser ! Received(ByteString(1,2,3,4,5))
        //then
        expectNoMsg()
      }
      "stop self on PeerClosed" in {
        //given
        val connection = TestProbe()
        val parser = TestActorRef(Props(new PbMessageParser(MessageClass(classOf[Person]), connection.ref)), self, "Parser4")
        val probe = TestProbe()
        probe watch parser
        //when
        parser ! PeerClosed
        //then
        probe.expectTerminated(parser)
      }
    }
}
