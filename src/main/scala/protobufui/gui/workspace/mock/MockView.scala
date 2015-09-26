package protobufui.gui.workspace.mock

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.scene.control.ContextMenu

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}


class MockView(workspaceEntry: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("New mock")

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = None

  override def detailsPath: String = "/fxml/mockPane.fxml"
}
