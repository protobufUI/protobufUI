package protobufui.gui.controllers

import java.net.{InetSocketAddress, URL}
import java.util.ResourceBundle
import java.{lang, util}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.beans.{InvalidationListener, Observable}
import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._
import javafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination, KeyEvent}

import akka.actor._
import com.google.protobuf.{MessageLite, TextFormat, UnknownFieldSet}
import ipetoolkit.details.DetailsController
import ipetoolkit.util.JavaFXDispatcher
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.Main
import protobufui.gui.controllers.MockTabController.{Start, Stop}
import protobufui.service.mock.{Mock, MockDefinition}
import protobufui.service.script.ScalaScriptingCtx
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass

import scala.collection.JavaConverters._

//TODO nie widzimy jesli mockowi nie uda sie zbindowac do portu
//TODO wyswietlanie obsluzonych requestow
class MockTabController extends Initializable with InvalidationListener with DetailsController {

  val mockSupervisor = Main.actorSystem.actorOf(Props(new MockSupervisor).withDispatcher(JavaFXDispatcher.Id))
  lazy val scriptCtx = new ScalaScriptingCtx //TODO trwa długo i przywiesi aplikacje przy pierwszym uzyciu(czyli w losowym momencie)
  @FXML var nameField: TextField = _
  @FXML var portField: TextField = _
  @FXML var responseClassCombo: ComboBox[MessageClass] = _
  @FXML var responseTextArea: TextArea = _
  @FXML var startStopToggle: ToggleButton = _
  @FXML var completionListView: ListView[String] = _
  @FXML var literalResponseRadio: RadioButton = _
  @FXML var scriptResponseRadio: RadioButton = _
  

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

    val ctrlSpaceKeyComb = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN)
    responseTextArea.setOnKeyPressed(new EventHandler[KeyEvent] {
      override def handle(event: KeyEvent): Unit = {
        if (ctrlSpaceKeyComb.`match`(event) && scriptResponseRadio.isSelected) {
          completeResponseScript()
        }
      }
    })
    ClassesContainer.addListener(this)
    synchronizeWithClassContainer()
    responseClassCombo.getSelectionModel.select(0)
  }


  override def invalidated(observable: Observable)={
    synchronizeWithClassContainer()
  }

  def synchronizeWithClassContainer(): Unit = {
    responseClassCombo.getItems.setAll(ClassesContainer.getClasses.asJavaCollection)
  }

  def disableMockDefControls(value: Boolean) = {
    portField.setDisable(value)
    responseClassCombo.setDisable(value)
    responseTextArea.setDisable(value)
    completionListView.setDisable(value)
    literalResponseRadio.setDisable(value)
    scriptResponseRadio.setDisable(value)
  }

  def createMockDefinition = {
    val port = portField.getText.toInt
    val responseGen = if (literalResponseRadio.isSelected) parseResponseLiteral() else if (scriptResponseRadio.isSelected) parseResponseScript else throw new UnsupportedOperationException
    MockDefinition(new InetSocketAddress(port), classOf[UnknownFieldSet], responseGen)
  }

  def parseResponseScript: PartialFunction[MessageLite, MessageLite] = {
    val responseBuilder = responseClassCombo.getSelectionModel.getSelectedItem.getBuilder
    val lines = responseTextArea.getText.split("\n")
    val result: PartialFunction[MessageLite, MessageLite] = {
      case request: UnknownFieldSet =>
        val scripting = new ScalaScriptingCtx
        scripting.engine.bind("request", request)
        scripting.engine.eval(s"import ${responseBuilder.getClass.getCanonicalName}")
        scripting.engine.bind("response", responseBuilder.getClass.getCanonicalName, responseBuilder)
        lines.take(lines.length).foreach(scripting.engine.interpret)
        scripting.engine.eval("response").asInstanceOf[MessageLite.Builder].build()
    }
    result
  }

  def parseResponseLiteral(): PartialFunction[MessageLite, MessageLite] = {
    val responseBuilder = responseClassCombo.getValue.getBuilder
    TextFormat.getParser.merge(responseTextArea.getText, responseBuilder)
    val response = responseBuilder.build()
    val result: PartialFunction[MessageLite, MessageLite] = {
      case request: UnknownFieldSet =>
        response
    }
    result
  }

  override def setModel(entry: WorkspaceEntry) = {
    super.setModel(entry)
    //nameField.textProperty().bindBidirectional(model.view.nameProperty)
    //TODO SAVE BUTTON !!
  }


  private def completeResponseScript(): Unit = {
    scriptCtx.engine.reset()
    scriptCtx.engine.bind("request", UnknownFieldSet.getDefaultInstance)

    val responseBuilder = responseClassCombo.getSelectionModel.getSelectedItem.getBuilder
    scriptCtx.engine.eval(s"import ${responseBuilder.getClass.getCanonicalName}")
    scriptCtx.engine.bind("response", responseBuilder.getClass.getCanonicalName, responseBuilder)

    scriptCtx.engine.get("response")
    val lines = responseTextArea.getText.split("\n")
    lines.take(lines.length - 1).foreach(scriptCtx.engine.interpret) //TODO we should check to the caret position not to the last line
    val candidates = new util.ArrayList[CharSequence]()
    scriptCtx.completion.complete(lines.takeRight(1).head, lines.takeRight(1).head.length, candidates)
    completionListView.getItems.setAll(candidates.asScala.map(_.toString): _*)
  }

  @FXML
  def literalSelected(): Unit = {
    completionListView.setVisible(false)
    responseTextArea.setText(responseClassCombo.getValue.getInstanceFilledWithDefaults.toString)
  }
  @FXML
  def scriptSelected(): Unit = {
    completionListView.setVisible(true)
    //TODO wstawic przykładowy działający skrypt
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





