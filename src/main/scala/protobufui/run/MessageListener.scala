package protobufui.run

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import com.google.protobuf.UnknownFieldSet
import protobufui.service.mock.PbMessageParser.Parsed
import protobufui.service.mock.{Mock, MockDefinition}
import protobufui.util.Propagator.{Propagated, RegisterListener}


object MessageListener extends App {

  if (args.length != 1) {
    Console.out.println("Jako arguemnt podaj numer portu")
    System.exit(1)
  }

  class Printer extends Actor {
    def receive = {
      case Propagated(Parsed(msg, _)) =>
        Console.out.println("!!MESSAGE!!")
        Console.out.println(msg.toString)
        Console.out.println("===========")
    }
  }

  val port = args(0).toInt
  val socketAddress = new InetSocketAddress("localhost", port)

  val mockDef = MockDefinition(socketAddress, classOf[UnknownFieldSet], { case _ => UnknownFieldSet.getDefaultInstance })

  val actorSystem = ActorSystem("MessageListener")

  val mock = actorSystem.actorOf(Props(new Mock(mockDef)), "mock")
  val printer = actorSystem.actorOf(Props(new Printer), "printer")

  mock ! RegisterListener(printer)
  //Testing part
  //val sender = actorSystem.actorOf(Props(new Sender(socketAddress)), "sender")
  //val msg = Person.newBuilder().setId(1).setName("JK").build()
  //import scala.concurrent.duration._
  //import scala.concurrent.ExecutionContext.Implicits.global
  //actorSystem.scheduler.schedule(1 seconds, 5 seconds, sender, ByteString(msg.toByteArray) )

  sys.addShutdownHook {
    println("Shutting down ...")
    actorSystem.shutdown()
    actorSystem.awaitTermination()
  }

  actorSystem.awaitTermination()
}

//TODO wyniesc do osobnej klasy i zrobic porzadnie
/*
class Sender(remote: InetSocketAddress) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      context stop self

    case c @ Connected(remotee, local) =>
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case _: ConnectionClosed =>
          context stop self
      }
  }
}*/
