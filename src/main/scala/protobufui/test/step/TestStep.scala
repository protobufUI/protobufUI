package protobufui.test.step

import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType

import scala.concurrent.Future


case class TestStepResult(step: TestStep, result: ResultType)

case class TestStepContext()

trait TestStep {

  def name: String

  def run(context: TestStepContext): Future[(ResultType.ResultType, TestStepContext)]

}
