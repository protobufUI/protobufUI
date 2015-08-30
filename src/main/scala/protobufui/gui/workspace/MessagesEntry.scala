package protobufui.gui.workspace

import javafx.scene.control.ContextMenu

import _root_.test.PbTest
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.service.source.ClassesContainer.MessageClass

import scala.xml.Elem


class MessagesEntry extends WorkspaceEntry {
  nameProperty.set("Messages")

  addChild(new MessageEntry(MessageClass(classOf[PbTest.Person])), None)

  //FIXME niech bedzie zawsze ba bierzaco z ClassContainerem.

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None

  override def detailsOpener: Option[Message] = None
}
