package protobufui.gui.workspace.base

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.service.test.TestSuite

class TestRoot extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Tests")

  override def model: WorkspaceEntry = new DummyModel(this)

  override def contextMenu: Option[ContextMenu] = {
    val newTestSuite = new MenuItem("New Test Suite")
    newTestSuite.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addWorkSpaceEntry(new TestSuite())
      }
    })
    Some(new ContextMenu(newTestSuite))
  }
}
