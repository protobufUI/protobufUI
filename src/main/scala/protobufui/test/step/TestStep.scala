package protobufui.test.step

import protobufui.test.step.ResultType.ResultType

/**
 * Created by krever on 9/27/15.
 */

object ResultType extends Enumeration {
  type ResultType = Value
  val Success, Failure, Empty = Value
}

case class TestStepResult(result: ResultType)

case class TestStepContext()

trait TestStep {

  def name: String

  def run(context: TestStepContext): (TestStepResult, TestStepContext)

}
