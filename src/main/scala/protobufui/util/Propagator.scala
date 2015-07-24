package protobufui.util

import akka.actor.ActorRef
import protobufui.util.Propagator.{Propagated, RegisterListener, UnregisterListener}


object Propagator {

  case class RegisterListener(listener: ActorRef)

  case class UnregisterListener(listener: ActorRef)

  case class Propagated(msg: Any)

}

trait Propagator {

  private var listeners: List[ActorRef] = List()

  protected val listenerManagement: PartialFunction[Any, Unit] = {
    case RegisterListener(listener) => listeners = listeners :+ listener
    case UnregisterListener(listener) => listeners = listeners.filterNot(_ == listener)
  }

  protected def propagate(msg: Any): Unit = {
    listeners.foreach(_ ! Propagated(msg))
  }

}
