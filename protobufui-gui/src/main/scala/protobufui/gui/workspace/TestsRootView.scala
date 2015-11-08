package protobufui.gui.workspace

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.model.TestSuite


class TestsRootView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override def contextMenu: Option[ContextMenu] = {
    val newTestSuite = new MenuItem("New Test Suite")
    newTestSuite.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addChild(new TestSuite("New Test Suite"))
      }
    })
    Some(new ContextMenu(newTestSuite))
  }

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: TestSuite => new TestSuiteView(x)
  }
}
