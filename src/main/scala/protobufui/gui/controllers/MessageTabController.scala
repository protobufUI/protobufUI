package protobufui.gui.controllers

import java.net.{InetSocketAddress, URL}
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{ComboBox, TextArea, TextField}

import akka.actor.{Actor, Props, Terminated}
import akka.util.ByteString
import com.google.protobuf.{Message, MessageLite, TextFormat}
import ipetoolkit.util.JavaFXDispatcher
import ipetoolkit.workspace.{DetailsController, WorkspaceEntry}
import protobufui.Main
import protobufui.service.message.MessageEntry
import protobufui.service.socket.TcpMessageSender
import protobufui.service.socket.TcpMessageSender._
import protobufui.service.source.ClassesContainer
import protobufui.service.source.ClassesContainer.MessageClass

import scala.util.Try


class MessageTabController extends Initializable with DetailsController {

  @FXML var hostnameField: TextField = _
  @FXML var portField: TextField = _
  @FXML var requestArea: TextArea = _
  @FXML var responseTypeCombo: ComboBox[MessageClass] = _
  @FXML var responseArea: TextArea = _

  var messageEntry: MessageEntry = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    import scala.collection.JavaConverters._
    responseTypeCombo.getItems.setAll(ClassesContainer.getClasses.asJavaCollection)
  }

  def sendMessage(): Unit = {
    val address = new InetSocketAddress(hostnameField.getText, portField.getText.toInt)
    val requestBuilder = messageEntry.messageClass.getBuilder
    TextFormat.getParser.merge(requestArea.getText, requestBuilder)
    Main.actorSystem.actorOf(Props(new MessageSenderSupervisor(address, requestBuilder.build())).withDispatcher(JavaFXDispatcher.Id))
  }

  def onResponse(response: ByteString) = {
    val responseMessage = Try(responseTypeCombo.getValue.getBuilder.mergeFrom(response.toArray).build())
    val responseText = responseMessage.transform((m: Message) => Try(TextFormat.printToString(m)), e => Try(e.toString))
    responseText.foreach(responseArea.setText)
  }

  def onSendFailed() = {
    responseArea.setText("Send failed. Look into logs for details.")
  }

  override def setModel(entry: WorkspaceEntry) = {
    this.messageEntry = entry.asInstanceOf[MessageEntry]
    val newDefaultRequest = TextFormat.printToString(messageEntry.messageClass.getInstanceFilledWithDefaults)
    requestArea.setText(newDefaultRequest)
  }

  class MessageSenderSupervisor(address: InetSocketAddress, message: MessageLite) extends Actor {

    val tcpMessageSender = context.actorOf(Props(new TcpMessageSender(address)))

    context.watch(tcpMessageSender)

    override def receive: Receive = {
      case TcpMessageSenderReady =>
        tcpMessageSender ! SendMessage(message)
      case Response(data) =>
        onResponse(data)
        context.stop(self)
      case Terminated(`tcpMessageSender`) =>
        onSendFailed()
        context.stop(self)
    }

  }

}
