package protobufui.test

import ipetoolkit.workspace.{WorkspaceEntryView, WorkspaceEntry}
import protobufui.gui.workspace.test.TestSuiteView

class TestSuite extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestSuiteView(this)
}
