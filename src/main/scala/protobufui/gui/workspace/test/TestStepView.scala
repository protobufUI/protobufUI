package protobufui.gui.workspace.test

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.util.dialog.Dialog

class TestStepView(val model: WorkspaceEntry, val detailsFxmlPath: String, val name: String) extends WorkspaceEntryView {

  override val detailsPath = Some(detailsFxmlPath)

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
        model.remove()
      }
    })
    Some(new ContextMenu(rename, delete))
  }

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = PartialFunction.empty
}
