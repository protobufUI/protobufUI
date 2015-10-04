package protobufui.cli

import akka.actor.Actor
import protobufui.gui.workspace.base.RootEntry
import protobufui.test.{TestCaseResult, TestSuiteResult}

case class RunSuites()

class SuitesRunner(suitesNames: Seq[String], rootEntry: RootEntry) extends Actor {

  override def receive: Receive = {
    case RunSuites =>
      val suites = rootEntry.getTests.filter(s => suitesNames.contains(s.nameProperty.getValue))
      val results:List[TestSuiteResult] = suites.map(s => TestSuiteResult(s, List()))
      sender() ! results
  }

}
