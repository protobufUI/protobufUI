package protobufui.service.mock

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.mock.MockView

class MockEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new MockView(this)
}
