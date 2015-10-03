package protobufui.cli

import akka.actor.Actor
case class RunSuites()
class SuitesRunner(suites: List[String]) extends Actor{
   override def receive: Receive = ???
 }
