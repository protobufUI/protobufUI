package protobufui.model.steps

import javax.xml.bind.annotation.{XmlElement, XmlRootElement}

import com.google.protobuf.{Message, TextFormat}
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.model.MessageClass
import protobufui.service.message.ClassesContainer

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SendMessageStepEntry extends WorkspaceEntry {

  @XmlElement
  var ipAddress: String = "192.168.0.1"
  @XmlElement
  var ipPort: String = "80"
  @XmlElement
  var messageClassName: String = _
  @XmlElement
  var messageContent: String = _

  def this(name: String) {
    this()
    setName(name)
  }

  def message: Message = {
    val builder = messageClass.getBuilder
    TextFormat.merge(messageContent, builder)
    builder.build()
  }

  def messageClass: MessageClass = ClassesContainer.getClass(messageClassName)

}


