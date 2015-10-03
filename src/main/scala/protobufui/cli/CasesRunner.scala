package protobufui.cli

import akka.actor.Actor

case class RunCases()
class CasesRunner(cases: List[String]) extends Actor{
  override def receive: Receive = ???
}
