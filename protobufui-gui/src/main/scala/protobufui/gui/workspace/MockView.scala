package protobufui.gui.workspace

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}


class MockView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override def detailsPath = Some("/fxml/mockPane.fxml")

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = PartialFunction.empty
}
