package protobufui.gui.workspace

import javafx.scene.control.ContextMenu

import ipetoolkit.bus.ClassBasedEventBus
import ipetoolkit.util.Message
import ipetoolkit.workspace.WorkspaceEntry

import scala.xml.Elem

class TestStepEntry(implicit eventBus: ClassBasedEventBus) extends WorkspaceEntry {
  nameProperty.setValue("Test Step")

  override def detailsOpener: Option[Message] = ???

  override def toXml: Option[Elem] = ???

  override def contextMenu: Option[ContextMenu] = None
}
