package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.scene.control.ContextMenu

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}

class TestStepView(workspaceEntry: WorkspaceEntry, override val detailsPath: String) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Test Step")

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = None

}
