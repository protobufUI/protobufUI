package protobufui.gui.workspace.test

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.TestCaseEntry
import protobufui.util.dialog.Dialog

class TestSuiteView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override def contextMenu: Option[ContextMenu] = {
    val newTestCase = new MenuItem("New Test Case")
    newTestCase.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addChild(new TestCaseEntry("New Test Case"))
      }
    })
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
    Some(new ContextMenu(newTestCase, rename, delete))
  }

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: TestCaseEntry => new TestCaseView(x)
  }
}
