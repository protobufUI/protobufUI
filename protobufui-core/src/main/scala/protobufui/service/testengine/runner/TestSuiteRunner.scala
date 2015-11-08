package protobufui.service.testengine.runner

import akka.actor.ActorSystem
import protobufui.model.TestSuite
import protobufui.service.testengine.TestSuiteResult

import scala.concurrent.Future

/**
 * Created by krever on 11/7/15.
 */
class TestSuiteRunner(testSuite: TestSuite) {
  def run(implicit actorSystem: ActorSystem): Future[TestSuiteResult] = {
    import actorSystem.dispatcher
    val casesResult = Future.sequence(testSuite.getTestCases.map(new TestCaseRunner(_).run))
    casesResult.map(x => TestSuiteResult(testSuite, x))
  }
}
