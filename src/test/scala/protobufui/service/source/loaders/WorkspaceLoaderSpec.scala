package protobufui.service.source.loaders

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike, _}
import protobufui.service.source.{ClassesContainer, Load, Loaded}

class WorkspaceLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

     def this() = this(ActorSystem("WorkspaceLoaderSpec"))

     def fixture = new {
       val parent = TestProbe()
       val workspaceRoot = new File("src/test/resources/protobufui/service/source/workspace/")
       val props = Props(classOf[WorkspaceLoader],workspaceRoot)
       val className = "test.PersonTest$Person2"
     }

  "WorkspaceLoaderSpec" should {
     "load compiled classes from WORKSPACE directory" in {
       // arrange
       val f = fixture
       val workspaceLoader = TestActorRef(f.props, f.parent.ref, "WorkspaceLoaderSpec")
       // act
       workspaceLoader ! Load(null)
       // assert
       f.parent.expectMsg(Loaded)
       Assertions.assert(ClassesContainer.exists(f.className))
       val messageClass = ClassesContainer.getClass(f.className)
       val className: String = messageClass.clazz.getName
       Assertions.assert(f.className.equals(className))
     }
   }
}
