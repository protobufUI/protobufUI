package protobufui.test.step

import com.google.protobuf.MessageLite
import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType

import scala.concurrent.Future


case class TestStepResult(step: TestStep, result: ResultType)

case class TestStepContext(stepResponses: Map[String, MessageLite] = Map()) {
  def addResponse(s: String, lite: MessageLite): TestStepContext = this.copy(stepResponses = stepResponses + (s -> lite))
}

trait TestStep {

  def name: String

  def run(context: TestStepContext): Future[(ResultType.ResultType, TestStepContext)]

}
