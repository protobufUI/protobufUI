package protobufui.test

import javax.xml.bind.annotation.XmlRootElement

import akka.actor.ActorSystem
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestSuiteView

import scala.concurrent.Future

@XmlRootElement
class TestSuite extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestSuiteView(this)

  def run(actorSystem: ActorSystem): Future[TestSuiteResult] = {
    import actorSystem.dispatcher
    val casesResult = Future.sequence(getTestCases.map(_.run(actorSystem)))
    casesResult.map(x => TestSuiteResult(this, x))
  }

  def getTestCases: List[TestCaseEntry] = children.collect { case t: TestCaseEntry => t }
}
