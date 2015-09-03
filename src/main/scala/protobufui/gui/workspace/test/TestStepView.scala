package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.scene.control.ContextMenu

import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}

class TestStepView(workspaceEntry: WorkspaceEntry, stepType: String) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty(stepType + "Test Step")

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = None

  override def detailsOpener: Option[Message] = None
}
