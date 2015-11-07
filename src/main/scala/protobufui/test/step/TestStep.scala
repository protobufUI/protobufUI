package protobufui.test.step

import com.google.protobuf.MessageLite
import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType

import scala.concurrent.Future

case class TestStepResult(step: TestStep, result: ResultType)

case class TestStepContext(stepResponses: Map[String, MessageLite] = Map(), propertyValueMap : Map[String, String] = Map()) {

  def addResponse(s: String, lite: MessageLite): TestStepContext = this.copy(stepResponses = stepResponses + (s -> lite))

  def addProperties(propertyValueMap : Map[String, String]) = this.copy(propertyValueMap = propertyValueMap++propertyValueMap)
}

trait TestStep {

  def getName: String

  def run(context: TestStepContext): Future[(ResultType.ResultType, TestStepContext)]

}
