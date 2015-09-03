package protobufui.gui.workspace.base

import ipetoolkit.workspace.{WorkspaceEntryView, WorkspaceEntry}

class DummyModel(workspaceEntryView: WorkspaceEntryView) extends WorkspaceEntry {
  override val view: WorkspaceEntryView = workspaceEntryView
}
