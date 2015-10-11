package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{MenuItem, ContextMenu}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.util.dialog.Dialog

class TestStepView(workspaceEntry: WorkspaceEntry, override val detailsPath: String, val name: String) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty(name)

  override def model: WorkspaceEntry = workspaceEntry

  override def contextMenu: Option[ContextMenu] = {
    val rename = new MenuItem("Rename")
    rename.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        Dialog.rename(nameProperty)
      }
    })
    val delete = new MenuItem("Delete")
    delete.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        model.delete()
      }
    })
    Some(new ContextMenu(rename, delete))
  }
}
