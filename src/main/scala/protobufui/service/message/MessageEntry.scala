package protobufui.service.message

import ipetoolkit.workspace.{WorkspaceEntryView, WorkspaceEntry}
import protobufui.gui.workspace.message.MessageView
import protobufui.service.source.ClassesContainer.MessageClass

class MessageEntry(val messageClass: MessageClass) extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new MessageView(this, messageClass)
}
