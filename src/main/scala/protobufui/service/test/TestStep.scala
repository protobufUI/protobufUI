package protobufui.service.test

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.workspace.test.TestStepView

class TestStep(testStepType: TestStepType.Value) extends WorkspaceEntry {
  override val view = new TestStepView(this, testStepType.toString)
}

object TestStepType extends Enumeration {
  val Message, Params, Validation = Value
}

