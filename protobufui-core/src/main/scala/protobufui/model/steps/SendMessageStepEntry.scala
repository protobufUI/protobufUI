package protobufui.model.steps

import java.net.InetSocketAddress
import javax.xml.bind.annotation.{XmlElement, XmlRootElement}

import akka.actor.Props
import akka.util.ByteString
import com.google.protobuf.{TextFormat, UnknownFieldSet, MessageLite, Message}
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.model.MessageClass
import protobufui.service.message.ClassesContainer

import scala.concurrent.{Future, Promise}

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SendMessageStepEntry extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }

  @XmlElement
  var ipAddress : String = "192.168.0.1"
  @XmlElement
  var ipPort : String = "80"
  @XmlElement
  var messageClassName: String = _
  @XmlElement
  var messageContent: String = _


  def messageClass: MessageClass = ClassesContainer.getClass(messageClassName)

  def message: Message = {
    val builder = messageClass.getBuilder
    TextFormat.merge(messageContent, builder)
    builder.build()
  }

}


