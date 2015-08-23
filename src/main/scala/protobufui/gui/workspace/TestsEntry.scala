package protobufui.gui.workspace

import javafx.event.{EventHandler, ActionEvent}
import javafx.scene.control.{MenuItem, ContextMenu}

import ipetoolkit.bus.ClassBasedEventBus
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import ipetoolkit.workspace.WorkspaceManagement.AddWorkspaceEntry

import scala.xml.Elem


class TestsEntry(implicit eventBus: ClassBasedEventBus) extends WorkspaceEntry {
  nameProperty.setValue("Tests")

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = Some(createContextMenu)

  private def createContextMenu = {
    val newTestSuite = new MenuItem("New Test Suite")
    newTestSuite.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        eventBus.publish(AddWorkspaceEntry(new TestSuiteEntry, Some(uid)))
      }
    })
    new ContextMenu(newTestSuite)
  }

  override def detailsOpener: Option[Message] = None
}

