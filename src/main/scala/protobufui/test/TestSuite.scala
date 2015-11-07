package protobufui.test

import javax.xml.bind.annotation.XmlRootElement

import akka.actor.ActorSystem
import ipetoolkit.workspace.WorkspaceEntry

import scala.concurrent.Future

@XmlRootElement
class TestSuite extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }

  def run(actorSystem: ActorSystem): Future[TestSuiteResult] = {
    import actorSystem.dispatcher
    val casesResult = Future.sequence(getTestCases.map(_.run(actorSystem)))
    casesResult.map(x => TestSuiteResult(this, x))
  }

  def getTestCases: List[TestCaseEntry] = children.collect { case t: TestCaseEntry => t }
}
