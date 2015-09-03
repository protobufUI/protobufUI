package protobufui.gui.workspace.base

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}

import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.service.message.MessageEntry
import protobufui.service.source.ClassesContainer.MessageClass
import test.PbTest


class MessageRoot() extends WorkspaceEntryView {

  //FIXME niech bedzie zawsze na bierzaco z ClassContainerem.
  addWorkSpaceEntry(new MessageEntry(MessageClass(classOf[PbTest.Person])))

  override val nameProperty: StringProperty = new SimpleStringProperty("Messages")

  override def model: WorkspaceEntry = new DummyModel(this)

  override def contextMenu: Option[ContextMenu] = {
    val newMessage = new MenuItem("New Message")
    newMessage.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addWorkSpaceEntry(new MessageEntry(MessageClass(classOf[PbTest.Person])))
      }
    })
    Some(new ContextMenu(newMessage))
  }

  override def detailsOpener: Option[Message] = None
}
