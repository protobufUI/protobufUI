package protobufui.service.mock

import akka.actor.ActorSystem
import akka.io.Tcp.Write
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import akka.util.ByteString
import com.google.protobuf.{MessageLite, UnknownFieldSet}
import org.mockito.Mockito
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.mock.PbMessageResponder.Respond
import test.PbTest.Person


class PbMessageResponderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("PbMessageResponderSpec"))


  "PbMessageResponder" should {

    "respond with given function" in {
      //given
      val request = Person.newBuilder().setId(1).setName("request").build()
      val response = Person.newBuilder().setId(1).setName("response").build()
      val responseGen = Mockito.mock(classOf[PartialFunction[MessageLite, MessageLite]])
      Mockito.when(responseGen.apply(request)).thenReturn(response)
      val responder = TestActorRef(new PbMessageResponder(responseGen))
      val connection = TestProbe()

      //when
      responder ! Respond(request, connection.ref)

      //then
      Mockito.verify(responseGen).apply(request)
      connection.expectMsg(Write(ByteString(response.toByteArray)))
    }

    "ignore invalid messages" in {
      //given
      val responseGen = Mockito.mock(classOf[PartialFunction[MessageLite, MessageLite]])
      val responder = TestActorRef(new PbMessageResponder(responseGen))
      val connection = TestProbe()

      //when
      responder ! Respond(UnknownFieldSet.getDefaultInstance, connection.ref)

      //then
      Mockito.verifyZeroInteractions(responseGen)
      connection.expectNoMsg()
    }

  }

}
