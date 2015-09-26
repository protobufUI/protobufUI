package protobufui.gui.workspace.base

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.TestSuite


@XmlRootElement
class TestsRootEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestsRootView(this)
}

class TestsRootView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Tests")

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
