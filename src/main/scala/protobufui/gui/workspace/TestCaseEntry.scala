package protobufui.gui.workspace

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{MenuItem, ContextMenu}

import ipetoolkit.bus.ClassBasedEventBus
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import ipetoolkit.workspace.WorkspaceManagement.AddWorkspaceEntry

import scala.xml.Elem

class TestCaseEntry(implicit eventBus: ClassBasedEventBus) extends WorkspaceEntry {
  nameProperty.setValue("Test Case")

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = Some(createContextMenu)

  private def createContextMenu = {
    val newTestStep = new MenuItem("New Test Step")
    newTestStep.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        eventBus.publish(AddWorkspaceEntry(new TestStepEntry, Some(uid)))
      }
    })
    new ContextMenu(newTestStep)
  }

  override def detailsOpener: Option[Message] = None
}
