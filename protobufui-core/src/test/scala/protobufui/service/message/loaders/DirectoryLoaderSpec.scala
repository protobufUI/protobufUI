package protobufui.service.message.loaders

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike, _}
import protobufui.service.message.{ClassesContainer, Load, Loaded}

class DirectoryLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

     def this() = this(ActorSystem("DirectoryLoaderSpec"))

     def fixture = new {
       val s = File.separator
       val parent = TestProbe()
       val workspaceRoot = new File(s"test${s}testWorkspace${s}") //TODO wrzucic do targetu bo smieci
       val props = Props(classOf[DirectoryLoader],workspaceRoot)
       val directory = new File(this.getClass.getResource("/protobufui/service/source").getFile)
       val className: String = "test.PersonTest$Person2"
     }

  "DirectoryLoaderSpec" should {
     "compile and load classes from directory" in {
       // arrange
       val f = fixture
       val directoryLoader = TestActorRef(f.props, f.parent.ref, "DirectoryLoaderSpec")
       // act
       directoryLoader ! Load(f.directory)
       // assert
       f.parent.expectMsg(Loaded)
       Assertions.assert(ClassesContainer.exists(f.className))
       val clazz = ClassesContainer.getInstanceOf(f.className)
     }
   }
}
