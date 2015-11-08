package protobufui.service.message

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestProbe, ImplicitSender, TestKit}
import org.scalatest.{Matchers, WordSpecLike}

class ClassesLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {
  def this() = this(ActorSystem("ClassesLoaderSpec"))

  def fixture = new {
    val parent = TestProbe()
    val workspaceStorage = new File("./test/workspaceRoot/compiledClasses/")
    val props = Props(new ClassesLoader(workspaceStorage))

    val jarFile = new File("./test.jar")
  }

  "ClassesLoader" should {
    "Send message about loading directory to children" in {
      // arrange
      val f = fixture
      val jarLoader = TestActorRef(f.props, f.parent.ref, s"$ClassesLoader")
      // act
      jarLoader ! Load(f.jarFile)
      // assert
    }
  }
}
