package protobufui.service.testengine.runner.step

import java.net.InetSocketAddress

import akka.actor.Props
import akka.util.ByteString
import com.google.protobuf.{MessageLite, UnknownFieldSet}
import protobufui.model.steps.SendMessageStepEntry
import protobufui.service.socket.MessageSenderSupervisor
import protobufui.service.testengine.ResultType.ResultType
import protobufui.service.testengine.{ResultType, TestStepContext}

import scala.concurrent.{Future, Promise}

/**
 * Created by krever on 11/8/15.
 */
class SendMessageStepRunner(step: SendMessageStepEntry) extends TestStepRunner {
  def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    val promise = Promise[(ResultType, TestStepContext)]()
    val address = new InetSocketAddress(step.ipAddress, step.ipPort.toInt)

    def onResponse(responseBytes: ByteString):Unit = {
      val response: MessageLite = UnknownFieldSet.parseFrom(responseBytes.toArray)
      promise.complete(scala.util.Success((ResultType.Success, context.addResponse(step.getName, response))))
    }
    def onSendFailed():Unit = {
      promise.complete(scala.util.Success((ResultType.Failure, context)))
    }

    context.actorSystem.actorOf(Props(new MessageSenderSupervisor(address, step.message, onResponse, onSendFailed)))
    promise.future
  }
}
