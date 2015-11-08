package protobufui.gui.controllers

import java.lang
import java.net.URL
import java.util.ResourceBundle
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.control._

import ipetoolkit.details.DetailsController
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.util.Editable
import protobufui.model.MessageClass
import protobufui.model.steps.SendMessageStepEntry
import protobufui.service.message.ClassesContainer

import scala.collection.JavaConverters._

/**
  * Created by humblehound on 26.09.15.
  */
class SendMessageStepController extends Initializable with DetailsController with Editable {

  @FXML var testName: TextField = _
  @FXML var messageArea: TextArea = _
  @FXML var ipAddress: TextField = _
  @FXML var ipPort: TextField = _
  @FXML var messageClassPicker: ComboBox[MessageClass] = _
  @FXML var sendBtn: Button = _
  var selectedMessageClass: MessageClass = _
  var sendMessageStep: SendMessageStepEntry = _

  def pageElements: List[Node] = List(testName, messageArea, ipAddress, ipPort, messageClassPicker)

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    initClassPicker()
    initAllControls()
  }

  private def initClassPicker(): Unit = {
    messageClassPicker.getItems.setAll(ClassesContainer.getClasses.asJavaCollection)
    messageClassPicker.selectionModelProperty().addListener(new ChangeListener[SingleSelectionModel[MessageClass]] {
      override def changed(observable: ObservableValue[_ <: SingleSelectionModel[MessageClass]], oldValue: SingleSelectionModel[MessageClass], newValue: SingleSelectionModel[MessageClass]): Unit =
        selectedMessageClass = newValue.getSelectedItem

      //messageArea.setText(MessageClass(???).getInstanceFilledWithDefaults.toString)
    })
  }

  private def initAllControls(): Unit = {
    saveBtn.disabledProperty().addListener(new ChangeListener[lang.Boolean] {
      override def changed(observable: ObservableValue[_ <: lang.Boolean], oldValue: lang.Boolean, newValue: lang.Boolean): Unit =
        sendBtn.setDisable(!newValue)
    })
    initEditionControls()
    sendBtn.setDisable(true)
  }

  override def setModel(entry: WorkspaceEntry) = {
    this.sendMessageStep = entry.asInstanceOf[SendMessageStepEntry]
    loadFormValuesFromTest()
  }

  override protected def onEditCompleted(): Unit = saveFormValuesToTest()

  private def saveFormValuesToTest() = {
    if (sendMessageStep != null) {
      sendMessageStep.setName(testName.getText)
      sendMessageStep.ipAddress = ipAddress.getText
      sendMessageStep.ipPort = ipPort.getText
      sendMessageStep.messageClassName = messageClassPicker.getSelectionModel.getSelectedItem.clazz.getName
      sendMessageStep.messageContent = messageArea.getText
    }
  }

  override protected def onEditCancelled(): Unit = loadFormValuesFromTest()

  private def loadFormValuesFromTest(): Unit = {
    ipAddress.setText(sendMessageStep.ipAddress)
    ipPort.setText(sendMessageStep.ipPort)
    testName.setText(sendMessageStep.getName)
    messageClassPicker.getSelectionModel.select(sendMessageStep.messageClass)
    messageArea.setText(sendMessageStep.messageContent)
  }

  override protected def onEditStart(): Unit = {}

  override protected def validate(): Boolean = true

  @FXML
  private def send(): Unit = {

  }
}
