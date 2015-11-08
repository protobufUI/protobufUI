package protobufui.service.testengine.runner.step

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.model.steps.{ScriptStepEntry, SetSpecsStepEntry, SendMessageStepEntry}

/**
 * Created by krever on 11/8/15.
 */
object TestStepRunnerFactory {

  def createRunner(testStep: WorkspaceEntry) = testStep match {
    case x: SendMessageStepEntry => new SendMessageStepRunner(x)
    case x: SetSpecsStepEntry=> new SetSpecsStepRunner(x)
    case x: ScriptStepEntry => new ScriptStepRunner(x)
  }

}
