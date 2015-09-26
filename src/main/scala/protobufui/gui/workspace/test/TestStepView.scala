package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.scene.control.ContextMenu

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.TestStepType.TestStepType

class TestStepView(workspaceEntry: WorkspaceEntry, stepType: TestStepType) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty(stepType + "Test Step")

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = None

  override def detailsPath: String = "/fxml/sendMessage.fxml"
}
