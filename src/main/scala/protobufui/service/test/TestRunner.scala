package protobufui.service.test

import akka.actor.Actor
import protobufui.service.test.TestRunner.{Run, TestStepRunResult}
import protobufui.test.step.ResultType.ResultType
import protobufui.test.step.{ResultType, TestStep, TestStepContext, TestStepResult}


object TestRunner {

  case class Run(steps: List[TestStep])

  case class Abort()

  //TODO not implemented

  case class TestStepRunResult(idx: Int, resultType: ResultType)

}

class TestRunner extends Actor {
  override def receive: Receive = {
    case Run(steps) =>
      steps.zipWithIndex.foldLeft((TestStepResult(ResultType.Empty), new TestStepContext)) {
        case ((TestStepResult(ResultType.Failure), ctx), _) => (TestStepResult(ResultType.Failure), ctx)

        case ((result, ctx), (step, idx)) =>
          val (result, newCtx) = step.run(ctx)
          sender() ! TestStepRunResult(idx, result.result)
          (result, newCtx)
      }

  }
}
