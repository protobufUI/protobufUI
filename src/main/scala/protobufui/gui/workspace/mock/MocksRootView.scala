package protobufui.gui.workspace.mock

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.service.mock.MockEntry


@XmlRootElement
class MocksRootEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new MocksRootView(this)
}

class MocksRootView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Mocks")

  override def contextMenu: Option[ContextMenu] = {
    val newMock = new MenuItem("New Mock")
    newMock.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addWorkSpaceEntry(new MockEntry())
      }
    })
    Some(new ContextMenu(newMock))
  }
}
