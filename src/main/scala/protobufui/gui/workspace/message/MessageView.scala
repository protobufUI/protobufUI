package protobufui.gui.workspace.message

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ContextMenu

import ipetoolkit.details.DetailsManagement.ShowDetails
import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.controllers.MessageTabController
import protobufui.service.message.MessageEntry
import protobufui.service.source.ClassesContainer.MessageClass


class MessageView(workspaceEntry: WorkspaceEntry, messageClass: MessageClass) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty(messageClass.clazz.getName)

  override def contextMenu: Option[ContextMenu] = None

  override def detailsOpener: Option[Message] = Some(ShowDetails(this, loadDetailsPane()))

  private def loadDetailsPane(): Node = {
    val loader = new FXMLLoader(getClass.getResource("/fxml/messagePane.fxml"))
    val pane = loader.load[Node]()
    val controller = loader.getController[MessageTabController]
    controller.setWorkspaceEntry(this.model.asInstanceOf[MessageEntry])

    pane
  }

  override def model: WorkspaceEntry = workspaceEntry


}
