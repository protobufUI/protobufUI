package protobufui.service.mock

import akka.actor.{Actor, ActorLogging, Props}
import com.google.protobuf.MessageLite
import protobufui.service.mock.PbMessageParser.Parsed
import protobufui.service.mock.PbMessageResponder.Respond
import protobufui.service.socket.TcpBond
import protobufui.service.socket.TcpBond.PeerConnected
import protobufui.util.Propagator

import scala.reflect.ClassTag

class Mock[Req <: MessageLite : ClassTag, Res <: MessageLite : ClassTag](mockDefinition: MockDefinition[Req, Res]) extends Actor with ActorLogging with Propagator {

  protected val responder = context.actorOf(Props(new PbMessageResponder[Req, Res](mockDefinition.responseGen)), s"Responder_${mockDefinition.hashCode()}")
  protected val tcpBond = context.actorOf(Props(new TcpBond(mockDefinition.socketAddress)), s"TcpBond_${mockDefinition.hashCode()}")

  override def receive: Receive = listenerManagement orElse {
    case p@PeerConnected(connection, msg) =>
      log.debug(s"Peer connected[${msg.remoteAddress}, ${msg.localAddress}")
      context.actorOf(Props(new PbMessageParser[Req](mockDefinition.reqClass, connection)), s"Parser_${p.hashCode()}")
    case p@Parsed(msg, connection) =>
      responder ! Respond(msg, connection)
      propagate(p)
  }


}
