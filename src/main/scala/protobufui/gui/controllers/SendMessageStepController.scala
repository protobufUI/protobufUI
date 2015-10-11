package protobufui.gui.controllers

import java.lang
import java.net.URL
import java.util.ResourceBundle
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.control._

import com.google.protobuf.TextFormat
import ipetoolkit.workspace.{DetailsController, WorkspaceEntry}
import protobufui.gui.Editable
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass
import protobufui.test.step.SendMessageStepEntry

/**
 * Created by humblehound on 26.09.15.
 */
class SendMessageStepController extends Initializable with DetailsController with Editable {

  @FXML var testName: TextField = _
  @FXML var messageArea: TextArea = _
  @FXML var ipAddress: TextField = _
  @FXML var ipPort: TextField = _
  @FXML var messageClassPicker: ComboBox[MessageClass] = _

  def pageElements : List[Node] = List(testName, messageArea, ipAddress, ipPort, messageClassPicker)

  @FXML var sendBtn: Button = _

  var selectedMessageClass: MessageClass = _

  var sendMessageStep: SendMessageStepEntry = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    initClassPicker()
    initAllControls()
  }

  private def initClassPicker(): Unit ={
    import scala.collection.JavaConverters._
    messageClassPicker.getItems.setAll(ClassesContainer.getClasses.asJavaCollection)
    messageClassPicker.selectionModelProperty().addListener(new ChangeListener[SingleSelectionModel[MessageClass]] {
      override def changed(observable: ObservableValue[_ <: SingleSelectionModel[MessageClass]], oldValue: SingleSelectionModel[MessageClass], newValue: SingleSelectionModel[MessageClass]): Unit =
        selectedMessageClass = newValue.getSelectedItem
        //messageArea.setText(MessageClass(???).getInstanceFilledWithDefaults.toString)
    })
  }

  private def initAllControls(): Unit ={
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

  private def saveFormValuesToTest() = {
    if (sendMessageStep != null) {
      sendMessageStep.nameProperty.setValue(testName.getText)
      sendMessageStep.ipAddress = ipAddress.getText
      sendMessageStep.ipPort = ipPort.getText
      sendMessageStep.messageClass = messageClassPicker.getSelectionModel.getSelectedItem
      val builder = sendMessageStep.messageClass.getBuilder
      TextFormat.getParser.merge(messageArea.getText, builder)
     sendMessageStep.message = builder.build()
    }
  }

  private def loadFormValuesFromTest(): Unit ={
    ipAddress.setText(sendMessageStep.ipAddress)
    ipPort.setText(sendMessageStep.ipPort)
    testName.setText(sendMessageStep.name)
    messageClassPicker.getSelectionModel.select(sendMessageStep.messageClass)
    if(sendMessageStep.message!= null) messageArea.setText(sendMessageStep.message.toString)
  }

  @FXML
  private def send(): Unit ={

  }

  override protected def onEditCompleted(): Unit = saveFormValuesToTest()

  override protected def onEditCancelled(): Unit = loadFormValuesFromTest()

  override protected def onEditStart(): Unit = {}

  override protected def validate(): Boolean = true
}
