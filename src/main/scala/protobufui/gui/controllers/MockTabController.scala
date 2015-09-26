package protobufui.gui.controllers

import java.net.{InetSocketAddress, URL}
import java.util.ResourceBundle
import java.{lang, util}
import javafx.beans.{Observable, InvalidationListener}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._
import javafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination, KeyEvent}

import akka.actor._
import com.google.protobuf.{MessageLite, UnknownFieldSet}
import ipetoolkit.util.JavaFXDispatcher
import protobufui.gui.Main
import protobufui.gui.controllers.MockTabController.{Start, Stop}
import protobufui.gui.workspace.mock.MockView
import protobufui.service.mock.{Mock, MockDefinition}
import protobufui.service.script.ScalaScriptingCtx
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass

import scala.collection.JavaConverters._

class MockTabController extends Initializable with InvalidationListener {

  val mockSupervisor = Main.actorSystem.actorOf(Props(new MockSupervisor).withDispatcher(JavaFXDispatcher.Id))
  val scriptCtx = new ScalaScriptingCtx
  var workspaceEntry: MockView = _
  @FXML var nameField: TextField = _
  @FXML var portField: TextField = _
  @FXML var responseTypeCombo: ComboBox[MessageClass] = _
  @FXML var responseTextArea: TextArea = _
  @FXML var startStopToggle: ToggleButton = _
  @FXML var completionListView: ListView[String] = _

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
    responseTypeCombo.getSelectionModel.select(0)

    val ctrlSpaceKeyComb = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN)
    responseTextArea.setOnKeyPressed(new EventHandler[KeyEvent] {
      override def handle(event: KeyEvent): Unit = {
        if (ctrlSpaceKeyComb.`match`(event)) {
          scriptCtx.engine.reset()
          scriptCtx.engine.bind("request", UnknownFieldSet.getDefaultInstance)

          val responseBuilder = responseTypeCombo.getSelectionModel.getSelectedItem.getBuilder
          scriptCtx.engine.eval(s"import ${responseBuilder.getClass.getCanonicalName}")
          val r = scriptCtx.engine.bind("response", responseBuilder.getClass.getCanonicalName, responseBuilder)

          val x = scriptCtx.engine.get("response")
          val lines = responseTextArea.getText.split("\n")
          lines.take(lines.length - 1).foreach(scriptCtx.engine.interpret) //TODO we should check to the caret position not to the last line
          var candidates = new util.ArrayList[CharSequence]()
          val completion = scriptCtx.completion.complete(lines.takeRight(1).head, lines.takeRight(1).head.length, candidates)
          completionListView.getItems.setAll(candidates.asScala.map(_.toString): _*)
        }
      }
    })
    ClassesContainer.addListener(this)
    synchronizeWithClassContainer
  }


  override def invalidated(observable: Observable)={
    synchronizeWithClassContainer
  }

  def synchronizeWithClassContainer: Unit = {
    responseTypeCombo.getItems.setAll(ClassesContainer.getClasses.asJavaCollection)
  }

  def disableMockDefControls(value: Boolean) = {
    portField.setDisable(value)
    responseTypeCombo.setDisable(value)
    responseTextArea.setDisable(value)
  }

  def createMockDefinition = {
    val port = portField.getText.toInt
    MockDefinition(new InetSocketAddress(port), classOf[UnknownFieldSet], requestToReponse)
  }

  def requestToReponse: PartialFunction[MessageLite, MessageLite] = {
    val responseBuilder = responseTypeCombo.getSelectionModel.getSelectedItem.getBuilder
    val lines = responseTextArea.getText.split("\n");
    {
      case request: UnknownFieldSet =>
        val scripting = new ScalaScriptingCtx
        scripting.engine.bind("request", request)
        scripting.engine.eval(s"import ${responseBuilder.getClass.getCanonicalName}")
        scripting.engine.bind("response", responseBuilder.getClass.getCanonicalName, responseBuilder)
        lines.take(lines.length).foreach(scripting.engine.interpret)
        scripting.engine.eval("response").asInstanceOf[MessageLite.Builder].build()
    }
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





