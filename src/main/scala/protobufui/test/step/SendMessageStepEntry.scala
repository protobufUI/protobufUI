package protobufui.test.step

import java.net.InetSocketAddress
import javax.xml.bind.annotation.{XmlElement, XmlRootElement}

import akka.actor.Props
import akka.util.ByteString
import com.google.protobuf.{Message, MessageLite, UnknownFieldSet}
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.Main
import protobufui.service.socket.MessageSenderSupervisor
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass
import protobufui.test.ResultType
import protobufui.test.ResultType._

import scala.concurrent.{Future, Promise}

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SendMessageStepEntry extends WorkspaceEntry with TestStep {

  def this(name:String){
    this()
    setName(name)
  }

  @XmlElement
  var ipAddress : String = "192.168.0.1"
  @XmlElement
  var ipPort : String = "80"
  var messageClass: MessageClass = _
  var message: Message = _

  
  override def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    val promise = Promise[(ResultType, TestStepContext)]()
    val address = new InetSocketAddress(ipAddress, ipPort.toInt)

    def onResponse(responseBytes: ByteString):Unit = {
      val response: MessageLite = UnknownFieldSet.parseFrom(responseBytes.toArray)
      promise.complete(scala.util.Success((ResultType.Success, context.addResponse(name, response))))
    }
    def onSendFailed():Unit = {
      promise.complete(scala.util.Success((ResultType.Failure, context)))
    }

    Main.actorSystem.actorOf(Props(new MessageSenderSupervisor(address, message, onResponse, onSendFailed)))
    promise.future
  }

  @XmlElement
  def getSerializedMessage: SerializedMessage = {
    val toSerialize = new SerializedMessage
    toSerialize.message = message.toByteArray
    toSerialize.name = messageClass.clazz.getName
    toSerialize
  }
  def setSerializedMessage(message: SerializedMessage) = {
    this.messageClass = ClassesContainer.getClass(message.name)
    this.message = messageClass.getBuilder.mergeFrom(message.message).build()
  }
}
@XmlRootElement
class SerializedMessage{
  @XmlElement
  var message:Array[Byte] = _
  @XmlElement
  var name:String = _
}

