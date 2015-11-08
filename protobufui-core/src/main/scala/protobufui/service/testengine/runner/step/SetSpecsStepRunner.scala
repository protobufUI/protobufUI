package protobufui.service.testengine.runner.step

import protobufui.model.steps.SetSpecsStepEntry
import protobufui.service.testengine.ResultType.ResultType
import protobufui.service.testengine.{ResultType, TestStepContext}

import scala.concurrent.Future

/**
 * Created by krever on 11/8/15.
 */
class SetSpecsStepRunner(step: SetSpecsStepEntry) extends TestStepRunner{

  def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    Future.successful((ResultType.Success, context.addProperties(step.propertyValueMap)))
  }

}
