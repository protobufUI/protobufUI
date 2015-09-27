package protobufui.gui.workspace.base

import java.io.File
import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.beans.{InvalidationListener, Observable}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javax.xml.bind.annotation.XmlRootElement

import akka.actor.Props
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.Globals
import protobufui.gui.Main
import protobufui.service.message.MessageEntry
import protobufui.service.source.ClassesLoader.Put
import protobufui.service.source.{ClassesContainer, ClassesLoader}

import scala.collection.JavaConverters._
import scala.language.implicitConversions


@XmlRootElement
class MessagesRootEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new MessagesRootView(this)
}

class MessagesRootView(val model: WorkspaceEntry) extends WorkspaceEntryView with InvalidationListener {
  private val workspaceRoot: File = new File(Globals.getProperty("workspace.root").get)
  val classesLoader = Main.actorSystem.actorOf(Props(new ClassesLoader(workspaceRoot)))

  override val nameProperty: StringProperty = new SimpleStringProperty("Messages")

  ClassesContainer.addListener(this)
  override def invalidated(observable: Observable): Unit = {
    ClassesContainer.getClasses.foreach { x => addWorkSpaceEntry(new MessageEntry(x)) }
  }

  override def contextMenu: Option[ContextMenu] = {
    val protoJARSelector = new MenuItem("Load Proto/JAR")
    protoJARSelector.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val fileChooser:FileChooser = new FileChooser()
        fileChooser.setTitle("Open Resource Dialog")
        val s: String = s"${File.separator}"
        //fileChooser.setInitialDirectory(new File(s"${s}src${s}test${s}resources${s}protobufui${s}service${s}source"))
        fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Select .proto or .jar file", "proto", "jar"))
        val files = fileChooser.showOpenMultipleDialog(null)
        files.asScala.foreach{classesLoader ! Put(_)}
        }
      }
    )
    Some(new ContextMenu(protoJARSelector))
  }

}
