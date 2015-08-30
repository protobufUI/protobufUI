package protobufui.gui.workspace

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ContextMenu

import ipetoolkit.details.DetailsManagement.ShowDetails
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.MockTabController

import scala.xml.Elem


class MockEntry extends WorkspaceEntry {
  nameProperty.setValue("New mock")

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None

  override val detailsOpener: Option[Message] = Some(ShowDetails(this, loadMockPane))

  def loadMockPane: Node = {
    val loader = new FXMLLoader(getClass.getResource("/fxml/mockPane.fxml"))
    val pane = loader.load[Node]()
    val controller = loader.getController[MockTabController]
    controller.setWorkspaceEntry(this)
    pane
  }
}
