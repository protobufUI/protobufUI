package protobufui.gui.workspace

import java.util.UUID
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.bus.ClassBasedEventBus
import ipetoolkit.util.Message
import ipetoolkit.workspace.{AddWorkspaceEntry, WorkspaceEntry}

import scala.xml.Elem

class MocksEntry(implicit eventBus: ClassBasedEventBus) extends WorkspaceEntry {
  override val uid: String = "mocksWorkspaceEntry"

  override def toXml: Option[Elem] = ???

  override val contextMenu: Option[ContextMenu] = Some(createContextMenu)

  override val detailsOpener: Option[Message] = None

  override val toString: String = "Mocks"

  def createContextMenu = {
    val newMock = new MenuItem("New Mock")
    newMock.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        eventBus.publish(AddWorkspaceEntry(new MockEntry(UUID.randomUUID().toString), Some(uid)))
      }
    })
    new ContextMenu(newMock)
  }
}
