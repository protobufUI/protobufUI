package protobufui.gui.workspace

import javafx.scene.control.ContextMenu

import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry

import scala.xml.Elem


class MessagesEntry extends WorkspaceEntry {
  nameProperty.set("Messages")

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None

  override def detailsOpener: Option[Message] = None
}
