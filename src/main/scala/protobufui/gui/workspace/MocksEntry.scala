package protobufui.gui.workspace

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry

import scala.xml.Elem

/**
 * Created by krever on 8/14/15.
 */
class MocksEntry extends WorkspaceEntry {
  override val uid: String = "mocksWorkspaceEntry"

  override def toXml: Option[Elem] = ???

  override val contextMenu: Option[ContextMenu] = Some(createContextMenu)

  override val detailsOpener: Option[Message] = None

  override val toString: String = "Mocks"

  def createContextMenu = {
    val newMock = new MenuItem("New Mock")
    newMock.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = ???
    })
    new ContextMenu(newMock)
  }
}
