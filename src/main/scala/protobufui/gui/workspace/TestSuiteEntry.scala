package protobufui.gui.workspace

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{MenuItem, ContextMenu}

import ipetoolkit.bus.ClassBasedEventBus
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import ipetoolkit.workspace.WorkspaceManagement.AddWorkspaceEntry

import scala.xml.Elem

class TestSuiteEntry(implicit eventBus: ClassBasedEventBus) extends WorkspaceEntry {
  nameProperty.setValue("Test Suite")

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = Some(createContextMenu)

  private def createContextMenu = {
    val newTestCase = new MenuItem("New Test Case")
    newTestCase.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        eventBus.publish(AddWorkspaceEntry(new TestCaseEntry, Some(uid)))
      }
    })
    new ContextMenu(newTestCase)
  }

  override def detailsOpener: Option[Message] = None
}

