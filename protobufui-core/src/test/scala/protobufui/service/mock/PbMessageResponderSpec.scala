package protobufui.service.mock

import akka.actor.ActorSystem
import akka.io.Tcp.Write
import akka.testkit.{TestProbe, TestActorRef, ImplicitSender, TestKit}
import akka.util.ByteString
import com.google.protobuf.{UnknownFieldSet, MessageLite}
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
      Mockito.when(responseGen.isDefinedAt(request)).thenReturn(true)
      val responder = TestActorRef(new PbMessageResponder(responseGen))
      val connection = TestProbe()

      //when
      responder ! Respond(request, connection.ref)

      //then
      Mockito.verify(responseGen).isDefinedAt(request)
      Mockito.verify(responseGen).apply(request)
      connection.expectMsg(Write(ByteString(response.toByteArray)))
    }

    "ignore invalid messages" in {
      //given
      val connection = TestProbe()
      val responseGen = Mockito.mock(classOf[PartialFunction[MessageLite, MessageLite]])
      val request = UnknownFieldSet.getDefaultInstance
      val responder = TestActorRef(new PbMessageResponder(responseGen))
      Mockito.when(responseGen.isDefinedAt(request)).thenReturn(true)
      //when
      responder ! Respond(request, connection.ref)

      //then
      Mockito.verify(responseGen).isDefinedAt(request)
      connection.expectNoMsg()
    }

  }

}
