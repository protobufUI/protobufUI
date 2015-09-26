package protobufui.gui.workspace.base

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.scene.control.ContextMenu

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.mock.MocksView

class Root extends WorkspaceEntryView {

  addWorkSpaceEntry(new MessageRoot().model)
  addWorkSpaceEntry(new MocksView().model)
  addWorkSpaceEntry(new TestRoot().model)

  override val nameProperty: StringProperty = new SimpleStringProperty("Root")

  override def model: WorkspaceEntry = new DummyModel(this)

  override def contextMenu: Option[ContextMenu] = None
}
