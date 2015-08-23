package protobufui.gui

import java.lang
import java.net.{InetSocketAddress, URL}
import java.util.ResourceBundle
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{ComboBox, TextArea, TextField, ToggleButton}

import akka.actor._
import com.google.protobuf.UnknownFieldSet
import ipetoolkit.util.JavaFXDispatcher
import protobufui.gui.MockTabController.{Start, Stop}
import protobufui.gui.workspace.MockEntry
import protobufui.service.mock.{Mock, MockDefinition}


class MockTabController extends Initializable {

  var workspaceEntry: MockEntry = _

  @FXML var nameField: TextField = _
  @FXML var portField: TextField = _
  @FXML var responseTypeCombo: ComboBox[Class[_]] = _
  @FXML var responseTextArea: TextArea = _
  @FXML var startStopToggle: ToggleButton = _

  val mockSupervisor = Main.actorSystem.actorOf(Props(new MockSupervisor).withDispatcher(JavaFXDispatcher.Id))


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
  }


  def disableMockDefControls(value: Boolean) = {
    portField.setDisable(value)
    responseTypeCombo.setDisable(value)
    responseTextArea.setDisable(value)
  }

  def createMockDefinition = {
    val port = portField.getText.toInt
    val response = UnknownFieldSet.getDefaultInstance //responseTypeCombo.getValue.newInstance().asInstanceOf[MessageLite].getDefaultInstanceForType //TODO sparsowac text areae
    MockDefinition[UnknownFieldSet, UnknownFieldSet](new InetSocketAddress(port), classOf[UnknownFieldSet], _ => response)
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

  def setWorkspaceEntry(entry: MockEntry) = {
    workspaceEntry = entry
    nameField.textProperty().bindBidirectional(workspaceEntry.nameProperty)
  }
}

object MockTabController {

  private case class Start()

  private case class Stop()

}





