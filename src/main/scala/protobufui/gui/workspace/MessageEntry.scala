package protobufui.gui.workspace

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ContextMenu

import ipetoolkit.details.DetailsManagement.ShowDetails
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.MessageTabController
import protobufui.service.source.ClassesContainer.MessageClass

import scala.xml.Elem


class MessageEntry(val messageClass: MessageClass) extends WorkspaceEntry {
  nameProperty.set(messageClass.clazz.getName)

  override def detailsOpener: Option[Message] = Some(ShowDetails(this, loadDetailsPane()))

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None


  private def loadDetailsPane(): Node = {
    val loader = new FXMLLoader(getClass.getResource("/fxml/messagePane.fxml"))
    val pane = loader.load[Node]()
    val controller = loader.getController[MessageTabController]
    controller.setWorkspaceEntry(this)
    pane
  }
}
