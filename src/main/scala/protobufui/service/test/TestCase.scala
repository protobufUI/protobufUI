package protobufui.service.test

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.workspace.test.TestCaseView

class TestCase extends WorkspaceEntry {
  override val view = new TestCaseView(this)
}

