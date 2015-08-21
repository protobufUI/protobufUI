package protobufui.gui.workspace

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ContextMenu

import ipetoolkit.details.DetailsManagement.ShowDetails
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry

import scala.xml.Elem


class MockEntry(name: String) extends WorkspaceEntry {
  override def uid: String = name

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None

  override def detailsOpener: Option[Message] = Some(ShowDetails(name, loadMockPane))

  def loadMockPane: Node = FXMLLoader.load(getClass.getResource("/mocks/mockPane.fxml"))
}
