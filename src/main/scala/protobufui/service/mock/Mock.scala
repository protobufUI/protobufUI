package protobufui.service.mock

import akka.actor.{Actor, ActorLogging, Props}
import protobufui.service.mock.PbMessageParser.Parsed
import protobufui.service.mock.PbMessageResponder.Respond
import protobufui.service.socket.TcpBond
import protobufui.service.socket.TcpBond.PeerConnected
import protobufui.util.Propagator

class Mock(mockDefinition: MockDefinition) extends Actor with ActorLogging with Propagator {

  protected val responder = context.actorOf(Props(new PbMessageResponder(mockDefinition.responseGen)), s"Responder_${mockDefinition.hashCode()}")
  protected val tcpBond = context.actorOf(Props(new TcpBond(mockDefinition.socketAddress)), s"TcpBond_${mockDefinition.hashCode()}")

  override def receive: Receive = listenerManagement orElse {
    case p@PeerConnected(connection, msg) =>
      log.debug(s"Peer connected[${msg.remoteAddress}, ${msg.localAddress}")
      context.actorOf(Props(new PbMessageParser(mockDefinition.requestClass, connection)), s"Parser_${p.hashCode()}")
    case p@Parsed(msg, connection) =>
      responder ! Respond(msg, connection)
      propagate(p)
  }


}
