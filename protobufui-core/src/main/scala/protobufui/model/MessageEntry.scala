package protobufui.model

import javafx.scene.control.TextArea
import javax.xml.bind.annotation.{XmlElement, XmlRootElement, XmlTransient}

import com.google.protobuf.TextFormat
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.service.message.ClassesContainer

@XmlRootElement
class MessageEntry extends WorkspaceEntry {

  def this(messageClass: MessageClass)={
    this
    this.messageClass = messageClass
    this.setName(uuid)
  }

  private var messageClass: MessageClass = _
  private var messageText:String = _
  private var textArea: TextArea = _

  override def uuid = messageClass.clazz.getName
  def initRequestArea(textArea: TextArea)={
    this.textArea = textArea
    if(messageText == null || messageText.isEmpty) {
      val newDefaultRequest = TextFormat.printToString(this.messageClass.getInstanceFilledWithDefaults)
      textArea.setText(newDefaultRequest)
    }else{
      textArea.setText(messageText)
    }
  }

  def setMessageText(msgText:String) = {
    this.messageText = msgText
  }
  @XmlElement
  def getMessageText : String = {
    try {
      textArea.getText
    }catch{
      case e:NullPointerException =>
        TextFormat.printToString(this.messageClass.getInstanceFilledWithDefaults)
    }
  }
  @XmlTransient
  def setMessageClass(msgClass:MessageClass) = {
    this.messageClass = msgClass
  }
  def getMessageClass: MessageClass = this.messageClass

  @XmlElement(name = "messageClass")
  def setMessageClassName(msgClassName:String) = {
    this.messageClass = ClassesContainer.getClass(msgClassName)
  }
  def getMessageClassName = messageClass.clazz.getName
}
