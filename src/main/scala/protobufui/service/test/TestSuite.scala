package protobufui.service.test

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.workspace.test.TestSuiteView

class TestSuite extends WorkspaceEntry {
  override val view = new TestSuiteView(this)
}

