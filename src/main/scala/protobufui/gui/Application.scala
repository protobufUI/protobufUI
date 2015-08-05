package protobufui.gui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._

private class ApplicationController() {

}


object Application extends JFXApp {

  stage = new JFXApp.PrimaryStage() {
    title = "Protobuf UI"
    width = 1024
    height = 768
    scene = new Scene {
      stylesheets += this.getClass.getResource("/css/ApplicationStyle.css").toExternalForm
      root = getContent
    }

  }

  val fooMenuItem = new MenuItem("foo")

  def getContent = new VBox() {
    vgrow = Priority.Always
    hgrow = Priority.Always
    children.add(getTopMenu)
    val layout: GridPane = getLayout
    layout.vgrow = Priority.Always
    children.add(layout)
  }

  def getTopMenu = new MenuBar {
    useSystemMenuBar = true
    menus = List(
      new Menu("File") {
        items = List(
          new MenuItem("New"),
          new MenuItem("Open"),
          new MenuItem("Save"),
          new MenuItem("Save As"),
          new SeparatorMenuItem,
          new MenuItem("Exit")
        )
      })
  }

  def getLayout = new GridPane {
    add(getProjectExplorer, 0, 0)
    add(getWorkspace, 1, 0)
  }


  def getProjectExplorer = new ScrollPane {
    fitToHeight = true
    vgrow = Priority.Always
    content = new TreeView[String] {
      prefWidth = 250
      showRoot = true
      root = new TreeItem[String]("Projects") {
        expanded = true
        children = getProjects
      }
    }
  }

  def getProjects = ObservableBuffer(
    new TreeItem[String]("Project 1"),
    new TreeItem[String]("Project 2"),
    new TreeItem[String]("Project 3")
  )

  def getWorkspace = new ScrollPane {
    fitToHeight = true
    fitToWidth = true
    vgrow = Priority.Always
    hgrow = Priority.Always
  }
}

