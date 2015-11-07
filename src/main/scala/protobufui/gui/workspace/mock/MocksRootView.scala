package protobufui.gui.workspace.mock

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.service.mock.MockEntry


@XmlRootElement
class MocksRootEntry extends WorkspaceEntry {
  setName("Mocks")
}

class MocksRootView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override def contextMenu: Option[ContextMenu] = {
    val newMock = new MenuItem("New Mock")
    newMock.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addChild(new MockEntry("New Mock"))
      }
    })
    Some(new ContextMenu(newMock))
  }

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: MockEntry => new MockView(x)
  }
}
