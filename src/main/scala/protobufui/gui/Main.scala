package protobufui.gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage


class Main extends Application {

  override def start(primaryStage: Stage): Unit = {
    val root: Parent = FXMLLoader.load(getClass.getResource("/main.fxml"))
    primaryStage.setTitle("protobufUI")
    primaryStage.setScene(new Scene(root, 800, 600))
    primaryStage.show()

    /*
        primaryStage.setOnCloseRequest(new EventHandler[WindowEvent] {
          override def handle(event: WindowEvent): Unit = {
            Global.actorSystem.shutdown()
          }
        })
    */

  }
}

object Main {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}
