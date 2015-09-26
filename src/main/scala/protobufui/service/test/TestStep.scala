package protobufui.service.test

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.workspace.test.TestStepView
import protobufui.test.TestStepType.TestStepType

class TestStep(testStepType: TestStepType) extends WorkspaceEntry {
  override val view = new TestStepView(this, testStepType)
}

object TestStepType extends Enumeration {
  val Message, Params, Validation = Value
}

