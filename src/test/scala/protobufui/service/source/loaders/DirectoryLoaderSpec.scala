package protobufui.service.source.loaders

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike, _}
import protobufui.service.source.ClassesLoader.Put
import protobufui.service.source.{ClassesContainer, Loaded, Load}

import scala.concurrent.duration.Duration

class DirectoryLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

     def this() = this(ActorSystem("DirectoryLoaderSpec"))

     def fixture = new {
       val parent = TestProbe()
       val workspaceRoot = new File("test/testWorkspace/")
       val props = Props(classOf[DirectoryLoader],workspaceRoot)
       val directory = new File("src/test/resources/protobufui/service/source/")
       val className: String = "test.PersonTest$Person2"
     }

  "DirectoryLoaderSpec" should {
     "classes from directory are compiled and loaded" in {
       // arrange
       val f = fixture
       val directoryLoader = TestActorRef(f.props, f.parent.ref, "DirectoryLoaderSpec")
       // act
       directoryLoader ! Load(f.directory)
       // assert
       f.parent.expectMsg(Loaded)
       Assertions.assert(ClassesContainer.exists(f.className))
     }
   }
}
