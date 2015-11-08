package protobufui.service.testengine.runner.step

import protobufui.service.testengine.ResultType._
import protobufui.service.testengine.TestStepContext

import scala.concurrent.Future

/**
 * Created by krever on 11/8/15.
 */
trait TestStepRunner {

  def run(context: TestStepContext): Future[(ResultType, TestStepContext)]

}
