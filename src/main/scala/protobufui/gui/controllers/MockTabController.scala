package protobufui.gui.controllers

import java.lang
import java.net.{InetSocketAddress, URL}
import java.util.ResourceBundle
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{ComboBox, TextArea, TextField, ToggleButton}

import akka.actor._
import com.google.protobuf.{TextFormat, UnknownFieldSet}
import ipetoolkit.util.JavaFXDispatcher
import protobufui.gui.Main
import protobufui.gui.controllers.MockTabController.{Stop, Start}
import protobufui.gui.workspace.mock.MockView
import protobufui.service.mock.{Mock, MockDefinition}
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass

import scala.collection.JavaConverters._

class MockTabController extends Initializable {

  val mockSupervisor = Main.actorSystem.actorOf(Props(new MockSupervisor).withDispatcher(JavaFXDispatcher.Id))
  var workspaceEntry: MockView = _
  @FXML var nameField: TextField = _
  @FXML var portField: TextField = _
  @FXML var responseTypeCombo: ComboBox[MessageClass] = _
  @FXML var responseTextArea: TextArea = _
  @FXML var startStopToggle: ToggleButton = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    startStopToggle.selectedProperty().addListener(new ChangeListener[lang.Boolean] {
      override def changed(observable: ObservableValue[_ <: lang.Boolean], oldValue: lang.Boolean, newValue: lang.Boolean): Unit = {
        if (newValue == true) {
          mockSupervisor ! Start
        } else {
          mockSupervisor ! Stop
        }
      }
    })
    responseTypeCombo.getItems.setAll(ClassesContainer.getClasses.asJavaCollection) //TODO aktualizcja na biezaco z ClassContainerem
  }


  def disableMockDefControls(value: Boolean) = {
    portField.setDisable(value)
    responseTypeCombo.setDisable(value)
    responseTextArea.setDisable(value)
  }

  def createMockDefinition = {
    val port = portField.getText.toInt
    val responseBuilder = responseTypeCombo.getValue.getBuilder
    TextFormat.getParser.merge(responseTextArea.getText, responseBuilder)
    val response = responseBuilder.build()
    MockDefinition(new InetSocketAddress(port), classOf[UnknownFieldSet], { case _ => response })
  }

  def setWorkspaceEntry(entry: MockView) = {
    workspaceEntry = entry
    nameField.textProperty().bindBidirectional(workspaceEntry.nameProperty)
  }

  private class MockSupervisor extends Actor with ActorLogging {

    def receive = waitForStart

    def waitForStart: Receive = {
      case Start =>
        val mock = context.actorOf(Props(new Mock(createMockDefinition)).withDispatcher(JavaFXDispatcher.Id))
        context.watch(mock)
        startStopToggle.setSelected(true)
        disableMockDefControls(true)
        context.become(monitor(mock))
    }

    def monitor(mock: ActorRef): Receive = {
      case Stop =>
        context.unwatch(mock)
        context.stop(mock)
        disableMockDefControls(false)
        startStopToggle.setSelected(false)
        context.become(waitForStart)
      case Terminated(`mock`) =>
        disableMockDefControls(false)
        startStopToggle.setSelected(false)
        context.become(waitForStart)
    }

  }
}

object MockTabController {

  private case class Start()

  private case class Stop()

}





