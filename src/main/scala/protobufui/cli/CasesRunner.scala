package protobufui.cli

import akka.actor.Actor
import protobufui.gui.workspace.base.RootEntry

case class RunCases()
class CasesRunner(cases: Seq[String], rootEntry: RootEntry) extends Actor{
  override def receive: Receive = ???
}
