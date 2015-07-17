package protobufui.service.mock

import akka.actor.{Actor, ActorLogging}
import com.google.protobuf.MessageLite

class Mock[Req <: MessageLite, Res <: MessageLite](mockDefinition: MockDefinition[Req, Res]) extends Actor with ActorLogging {

  //register handler
  //wait for parsed
  //send response

  override def receive: Receive = ???


}
