package protobufui.service.socket

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import akka.io.Tcp.{Bind, CommandFailed, Connected}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.socket.TcpBond.{BindingFailed, PeerConnected}

class TcpBondSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

  def this() = this(ActorSystem("TcpBondSpec"))

  "TcpBond" should {
    "bind to given address on start" in {
      //given
      val address = new InetSocketAddress("localhost", 12345)
      val ioTcpProbe = TestProbe()
      //when
      val bond = TestActorRef(new TcpBond(address) {
        override val ioTcp = ioTcpProbe.ref
      })
      //then
      ioTcpProbe.expectMsg(Bind(bond, address))
    }

    "forward Connected as PeerConected to parent" in {
      //given
      val address = new InetSocketAddress("localhost", 12346)
      val ioTcpProbe = TestProbe()
      val parent = TestProbe()
      val connectedMsg = Connected(address, address)
      //when
      val bond = TestActorRef(Props(new TcpBond(address) {
        override val ioTcp = ioTcpProbe.ref
      }), parent.ref, "TcpBond")
      ioTcpProbe.send(bond, connectedMsg)
      //then
      parent.expectMsg(PeerConnected(ioTcpProbe.ref, connectedMsg))
    }

    "signal and stop when binding failed" in {
      //given
      val address = new InetSocketAddress("localhost", 12347)
      val ioTcpProbe = TestProbe()
      val terminationProbe = TestProbe()
      //when
      val bond = TestActorRef(Props(new TcpBond(address) {
        override val ioTcp = ioTcpProbe.ref
      }), self, "TcpBond")
      terminationProbe.watch(bond)
      ioTcpProbe.send(bond, CommandFailed(Bind(bond, address)))
      //then
      expectMsg(BindingFailed(address))
      terminationProbe.expectTerminated(bond)
    }
  }

}
