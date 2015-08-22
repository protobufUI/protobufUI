package protobufui.gui.workspace

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.bus.ClassBasedEventBus
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import ipetoolkit.workspace.WorkspaceManagement.AddWorkspaceEntry

import scala.xml.Elem

class MocksEntry(implicit eventBus: ClassBasedEventBus) extends WorkspaceEntry {
  nameProperty.setValue("Mocks")

  override def toXml: Option[Elem] = ???

  override val contextMenu: Option[ContextMenu] = Some(createContextMenu)

  override val detailsOpener: Option[Message] = None


  private def createContextMenu = {
    val newMock = new MenuItem("New Mock")
    newMock.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        eventBus.publish(AddWorkspaceEntry(new MockEntry, Some(uid)))
      }
    })
    new ContextMenu(newMock)
  }
}
