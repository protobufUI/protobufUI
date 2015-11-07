package protobufui.gui.workspace.base

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestSuiteView
import protobufui.test.TestSuite


@XmlRootElement
class TestsRootEntry extends WorkspaceEntry {
  setName("Tests")
}

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
