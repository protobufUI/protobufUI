package protobufui.service.testengine.runner

import akka.actor.ActorSystem
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.model.TestCaseEntry
import protobufui.service.testengine.runner.step.TestStepRunnerFactory
import protobufui.service.testengine.{TestStepContext, TestStepResult, ResultType, TestCaseResult}

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by krever on 11/7/15.
 */
class TestCaseRunner(testCase: TestCaseEntry) {

  def run(implicit actorSystem: ActorSystem): Future[TestCaseResult] = {
    import actorSystem.dispatcher
    val casesResult = Future.sequence(runTests(testCase.getTestSteps))
    casesResult.map(x => TestCaseResult(testCase, x))
  }

  private def runTests(steps: List[WorkspaceEntry])(implicit actorSystem: ActorSystem): List[Future[TestStepResult]] = {
    val results = ListBuffer[Future[TestStepResult]]()
    import actorSystem.dispatcher
    steps.zipWithIndex.foldLeft(Future((ResultType.Empty, new TestStepContext))) {
      case (args, (step, idx)) =>
        val newResult = for {
          (prevResult, ctx) <- args
          (curResullt, newCtx) <- TestStepRunnerFactory.createRunner(step).run(ctx)
        } yield (curResullt, newCtx)
        results += newResult.map(x => new TestStepResult(step, x._1))
        newResult
    }

    results.toList
  }
}
