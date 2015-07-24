package protobufui.util

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.util.Propagator.{Propagated, RegisterListener, UnregisterListener}

/**
 * Created by krever on 7/24/15.
 */
class PropagatorSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("MessageActorSpec"))

  def testSubject = new Actor with Propagator {
    override def receive: Receive = listenerManagement orElse {
      case x => propagate(x)
    }
  }

  "Propagator" should {
    "deliver message to subscribed actor" in {
      //given
      val propagator = TestActorRef(testSubject)
      val listener = TestProbe()

      //when
      propagator ! RegisterListener(listener.ref)
      propagator ! "test"

      //then
      listener.expectMsg(Propagated("test"))
    }

    "not deliver message to unsubscribed actor" in {
      //given
      val propagator = TestActorRef(testSubject)
      val listener = TestProbe()

      //when
      propagator ! RegisterListener(listener.ref)
      propagator ! UnregisterListener(listener.ref)
      propagator ! "test"

      //then
      listener.expectNoMsg()
    }
  }

}
