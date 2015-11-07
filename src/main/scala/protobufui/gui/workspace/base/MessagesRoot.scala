package protobufui.gui.workspace.base

import java.io.File
import javafx.beans.{InvalidationListener, Observable}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.{DirectoryChooser, FileChooser}
import javax.xml.bind.annotation.XmlRootElement

import akka.actor.Props
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.{Globals, Main}
import protobufui.gui.workspace.message.MessageView
import protobufui.service.message.MessageEntry
import protobufui.service.source.ClassesLoader.{LoadWorkspace, Put}
import protobufui.service.source.{ClassesContainer, ClassesLoader}

import scala.collection.JavaConverters._
import scala.language.implicitConversions

@XmlRootElement
class MessagesRootEntry extends WorkspaceEntry {
  setName("Messages")
}

class MessagesRootView(val model: WorkspaceEntry) extends WorkspaceEntryView with InvalidationListener {
  private val workspaceRoot: File = new File(Globals.getProperty(Globals.Keys.workspaceRoot).get)
  val classesLoader = Main.actorSystem.actorOf(Props(new ClassesLoader(workspaceRoot)))

  classesLoader ! LoadWorkspace

  ClassesContainer.addListener(this)
  override def invalidated(observable: Observable): Unit = {
    ClassesContainer.getClasses.foreach { x => addChild(new MessageEntry(x)) }
  }

  override def contextMenu: Option[ContextMenu] = {
    Some(MessagesRootContextMenu.createContextMenu)
  }

  object MessagesRootContextMenu {
    def createContextMenu: ContextMenu = {
      val protoJARSelector: MenuItem = createProtoJarSelector
      val directorySelector: MenuItem = createDirectorySelector
      new ContextMenu(protoJARSelector/*, directorySelector*/)
    }

    def createProtoJarSelector: MenuItem = {
      val protoJARSelector = new MenuItem("Load Proto/JAR")
      protoJARSelector.setOnAction(new EventHandler[ActionEvent] {
        override def handle(event: ActionEvent): Unit = {
          val fileChooser: FileChooser = new FileChooser()
          fileChooser.setTitle("Open Resource Dialog")
          val s: String = s"${File.separator}"
          fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Select .proto or .jar file", "proto", "jar"))
          val files = fileChooser.showOpenMultipleDialog(null)
          files.asScala.foreach {
            classesLoader ! Put(_)
          }
        }})
      protoJARSelector
    }

    def createDirectorySelector: MenuItem = {
      val directorySelector = new MenuItem("Load directory")
      directorySelector.setOnAction(new EventHandler[ActionEvent] {
        override def handle(event: ActionEvent): Unit = {
          val directoryChooser: DirectoryChooser = new DirectoryChooser()
          directoryChooser.setTitle("Select directory with java classes")
          val directory = directoryChooser.showDialog(null)
          classesLoader ! Put(directory)
        }}
      )
      directorySelector
    }
  }

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: MessageEntry => new MessageView(x)
  }
}
