package protobufui.gui.workspace

import java.io.File
import javafx.beans.{InvalidationListener, Observable}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, MenuItem}
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.{DirectoryChooser, FileChooser}

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.Main
import protobufui.model.MessageEntry
import protobufui.service.message.ClassesLoader.{LoadFromWorkspace, Put}
import protobufui.service.message.{ClassesContainer, ClassesLoader}
import protobufui.util.Globals

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.implicitConversions



class MessagesRootView(val model: WorkspaceEntry) extends WorkspaceEntryView with InvalidationListener {
  private val workspaceRoot: File = new File(Globals.getProperty(Globals.Keys.workspaceRoot).get)
  val classesLoader = Main.actorSystem.actorOf(Props(new ClassesLoader(workspaceRoot)))
  implicit val timeout = Timeout(5 seconds)

  val result = Await.result(classesLoader ? LoadFromWorkspace, timeout.duration)


  // TODO znaleźć dlaczego do cholery jeden element dodaje się dwa razy
  result match {
    case protobufui.service.message.Loaded =>{
      ClassesContainer.getClasses.foreach {
        x => addChild(new MessageEntry(x))
      }
    }
  }

  ClassesContainer.addListener(this)
  override def invalidated(observable: Observable): Unit = {

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
