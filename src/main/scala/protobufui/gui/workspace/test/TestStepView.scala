package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ContextMenu

import ipetoolkit.details.DetailsManagement.ShowDetails
import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.controllers.SendMessageController
import protobufui.test.TestStepType.TestStepType

class TestStepView(workspaceEntry: WorkspaceEntry, stepType: TestStepType) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty(stepType + "Test Step")

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = None

  private def loadDetailsPane(): Node = {
    val loader = new FXMLLoader(getClass.getResource("/fxml/sendMessage.fxml"))
    val pane = loader.load[Node]()
    val controller = loader.getController[SendMessageController]
    controller.setModel(this.model)
    pane
  }
}
