package protobufui.run


import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}

object Main extends JFXApp {

  stage = new JFXApp.PrimaryStage() {
    title = "Protobuf UI"
    width = 800
    height = 600
    scene = new Scene(
      FXMLView(getClass.getResource("/gui/ApplicationFrame.fxml"), NoDependencyResolver)
    )
  }
}
