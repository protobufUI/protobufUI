package protobufui.gui.workspace.mock

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ContextMenu

import ipetoolkit.details.DetailsManagement.ShowDetails
import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.controllers.MockTabController


class MockView(workspaceEntry: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("New mock")
  override val detailsOpener: Option[Message] = Some(ShowDetails(this, loadMockPane))

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = None

  def loadMockPane: Node = {
    val loader = new FXMLLoader(getClass.getResource("/fxml/mockPane.fxml"))
    val pane = loader.load[Node]()
    val controller = loader.getController[MockTabController]
    controller.setWorkspaceEntry(this)
    pane
  }


}
