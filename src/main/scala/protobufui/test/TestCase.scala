package protobufui.test

import ipetoolkit.workspace.{WorkspaceEntryView, WorkspaceEntry}
import protobufui.gui.workspace.test.TestCaseView

class TestCase extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestCaseView(this)
}
