package protobufui.service.message

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.message.MessageView
import protobufui.service.source.ClassesContainer.MessageClass

//TODO @XmlRootElement
class MessageEntry(val messageClass: MessageClass) extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new MessageView(this, messageClass)

  override def uuid = messageClass.clazz.getName
}
