package protobufui.gui

import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.Menu
import scalafxml.core.macros.sfxml

@sfxml
class ApplicationController(private val mainMenu: Menu) {

  println("Init complete")

  def onOpenFile(event: ActionEvent) = {

  }

  def onExit(event: ActionEvent) = {
    Platform.exit()
  }

}
