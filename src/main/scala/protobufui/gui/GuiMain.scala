package protobufui.gui

import javafx.application.{Application, Platform}
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, WindowEvent}

import protobufui.util.dialog.Dialog


class GuiMain extends Application {

  override def start(primaryStage: Stage): Unit = {
    Dialog.setWorkspaceRoot()
    val root: Parent = FXMLLoader.load(getClass.getResource("/fxml/main.fxml"))
    primaryStage.setTitle("protobufUI")
    primaryStage.setScene(new Scene(root, 800, 600))
    primaryStage.getScene.getStylesheets.add("/css/ApplicationStyle.css")
    primaryStage.setOnCloseRequest(new EventHandler[WindowEvent] {
      override def handle(event: WindowEvent): Unit = {
        Platform.exit()
        System.exit(0)
      }
    })
    primaryStage.show()
  }
}
