package protobufui.test.step

import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType


case class TestStepResult(step: TestStep, result: ResultType)

case class TestStepContext()

trait TestStep {

  def name: String

  def run(context: TestStepContext): (ResultType.ResultType, TestStepContext)

}
