package protobufui.gui.controllers

import java.net.URL
import java.util.ResourceBundle
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass
import protobufui.test.SendMessageStep

/**
 * Created by humblehound on 26.09.15.
 */
class SendMessageController extends Initializable {

  @FXML var testName: TextField = _
  @FXML var messageArea: TextArea = _
  @FXML var ipAddress: TextField = _
  @FXML var ipPort: TextField = _
  @FXML var messageClassPicker: ComboBox[MessageClass] = _
  @FXML var editBtn: Button = _
  @FXML var cancelBtn: Button = _
  @FXML var sendBtn: Button = _
  @FXML var saveBtn: Button = _

  var selectedMessageClass: MessageClass = _
  var sendMessageStep : SendMessageStep = new SendMessageStep

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    import scala.collection.JavaConverters._
    messageClassPicker.getItems.setAll(ClassesContainer.getClasses.asJavaCollection)
    messageClassPicker.selectionModelProperty().addListener(new ChangeListener[SingleSelectionModel[MessageClass]] {
      override def changed(observable: ObservableValue[_ <: SingleSelectionModel[MessageClass]], oldValue: SingleSelectionModel[MessageClass], newValue: SingleSelectionModel[MessageClass]): Unit =
        selectedMessageClass = newValue.getSelectedItem
//        messageArea.setText(MessageClass(???).getInstanceFilledWithDefaults.toString)
    })
    cancelBtn.setDisable(true)
    sendBtn.setDisable(true)
    editBtn.setDisable(true)
    loadFormValuesFromTest()
  }

  def setModel(entry: WorkspaceEntry) = {
    this.sendMessageStep = entry.asInstanceOf[SendMessageStep]
  }

  @FXML
  private def save(): Unit ={
    if(validate()){
      saveFormValuesToTest()
      setEditEnabled(false)
    }else{
      // Show what's wrong
    }
  }

  private def validate(): Boolean ={
    return true
  }

  private def saveFormValuesToTest() = {
//    val builder = MessageClass(???).getBuilder
//    TextFormat.getParser.merge(messageArea.getText, builder)
  //
//    sendMessageStep.message = builder.build()
    sendMessageStep.testName = testName.getText
    sendMessageStep.ipAddress = ipAddress.getText
    sendMessageStep.ipPort = ipPort.getText
    sendMessageStep.testName = testName.getText
    sendMessageStep.messageClass = messageClassPicker.getSelectionModel.getSelectedItem
  }

  @FXML
  private def edit(): Unit ={
    setEditEnabled(true)
  }

  @FXML
  private def setEditEnabled(isEnabled: Boolean): Unit ={
    testName.setDisable(!isEnabled)
    ipAddress.setDisable(!isEnabled)
    ipPort.setDisable(!isEnabled)
    messageArea.setDisable(!isEnabled)
    messageClassPicker.setDisable(!isEnabled)

    cancelBtn.setDisable(!isEnabled)
    saveBtn.setDisable(!isEnabled)
    editBtn.setDisable(isEnabled)
    sendBtn.setDisable(isEnabled)
  }

  @FXML
  private def cancel(): Unit ={
    setEditEnabled(false)
    loadFormValuesFromTest()
  }

  private def loadFormValuesFromTest(): Unit ={
    ipAddress.setText(sendMessageStep.ipAddress)
    ipPort.setText(sendMessageStep.ipPort)
    testName.setText(sendMessageStep.testName)
    messageClassPicker.getSelectionModel.select(sendMessageStep.messageClass)
//    messageArea.setText(sendMessageStep.message.toString)
  }

  @FXML
  private def send(): Unit ={
    
  }
}
