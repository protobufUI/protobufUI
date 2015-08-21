package protobufui.gui.workspace

import javafx.scene.control.ContextMenu

import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry

import scala.xml.Elem

class RootEntry extends WorkspaceEntry {

  import protobufui.gui.Main.eventBus

  val messages = new MessagesEntry
  val tests = new TestsEntry
  val mocks = new MocksEntry
  treeItem.getChildren.addAll(messages.treeItem, tests.treeItem, mocks.treeItem)
  treeItem.setExpanded(true)

  override def uid: String = "rootWorkspaceEntry"

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None

  override val detailsOpener: Option[Message] = None

  override def toString(): String = "Root"
}
