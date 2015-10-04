package protobufui.test

import javax.xml.bind.annotation.XmlRootElement

import akka.actor.ActorSystem
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestCaseView
import protobufui.test.step.{TestStep, TestStepContext, TestStepResult}

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

@XmlRootElement
class TestCaseEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestCaseView(this)

  def run(actorSystem: ActorSystem): Future[TestCaseResult] = {
    import actorSystem.dispatcher
    val casesResult = Future.sequence(runTests(getTestSteps))
    casesResult.map(x => TestCaseResult(this, x))
  }

  def getTestSteps: List[TestStep] = children.collect { case t: TestStep => t }

  private def runTests(steps: List[TestStep])(implicit executor: ExecutionContext): List[Future[TestStepResult]] = {
    val results = ListBuffer[Future[TestStepResult]]()
    steps.zipWithIndex.foldLeft(Future((ResultType.Empty, new TestStepContext))) {
      case (args, (step, idx)) =>
        val newResult = for {
          (prevResult, ctx) <- args
          (curResullt, newCtx) <- step.run(ctx)
        } yield (curResullt, newCtx)
        results += newResult.map(x => new TestStepResult(step, x._1))
        newResult
    }

    results.toList
  }


}

