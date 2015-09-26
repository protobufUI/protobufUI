package protobufui.gui.workspace.mock

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.base.DummyModel
import protobufui.service.mock.MockEntry

class MocksView extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Mocks")

  override def model: WorkspaceEntry = new DummyModel(this)

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
