package protobufui.service.test

import akka.actor.Actor
import protobufui.service.test.TestRunner.{Run, TestStepRunResult}
import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType
import protobufui.test.step.{TestStep, TestStepContext}


object TestRunner {

  case class Run(steps: List[TestStep])

  case class Abort()

  //TODO not implemented

  case class TestStepRunResult(idx: Int, resultType: ResultType)

}

class TestRunner extends Actor {
  override def receive: Receive = {
    case Run(steps) =>
      steps.zipWithIndex.foldLeft((ResultType.Empty, new TestStepContext)) {
        case (r@(ResultType.Failure, ctx), _) => r

        case ((result, ctx), (step, idx)) =>
          val (result, newCtx) = step.run(ctx)
          sender() ! TestStepRunResult(idx, result)
          (result, newCtx)
      }

  }
}
