package protobufui.gui

import javafx.application.{Platform, Application}
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, WindowEvent}

import akka.actor.ActorSystem
import ipetoolkit.bus.{ClassBasedEventBus, IPEEventBus}


class Main extends Application {

  override def start(primaryStage: Stage): Unit = {
    val root: Parent = FXMLLoader.load(getClass.getResource("/fxml/main.fxml"))
    primaryStage.setTitle("protobufUI")
    primaryStage.setScene(new Scene(root, 800, 600))
    primaryStage.getScene.getStylesheets.add("/css/ApplicationStyle.css")
    primaryStage.setOnCloseRequest(new EventHandler[WindowEvent] {
      override def handle(event: WindowEvent): Unit = {
        Platform.exit()
      }
    })
    primaryStage.show()
  }
}

object Main {

  val actorSystem: ActorSystem = ActorSystem("protobufUI")
  implicit val eventBus: ClassBasedEventBus = IPEEventBus

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}
