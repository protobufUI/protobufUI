package protobufui.gui.workspace.message

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}


class MessageView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override def detailsPath = Some("/fxml/messagePane.fxml")

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = PartialFunction.empty
}
