package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.TestCaseEntry
import protobufui.util.dialog.Dialog

class TestSuiteView(workspaceEntry: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Test Suite")

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = {
    val newTestCase = new MenuItem("New Test Case")
    newTestCase.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addWorkSpaceEntry(new TestCaseEntry())
      }
    })
    val rename = new MenuItem("Rename")
    rename.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        Dialog.rename(nameProperty);
      }
    })
    val delete = new MenuItem("Delete")
    delete.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        model.delete()
      }
    })
    Some(new ContextMenu(newTestCase, rename, delete))
  }
}
