package protobufui.service.source

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Matchers, WordSpecLike}

/**
 * Created by pcejrowski on 2015-07-16.
 */
class JarLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

    def this() = this(ActorSystem("JarLoaderSpec"))

  /*def fixture = new {
    val parent = TestProbe()
    val workspaceStorage = new File("./test/compiledClasses/")
    val props = Props(new ClassesContainer(workspaceStorage))
    val jarFile = new File("./test.jar")
  }

  "JarLoaderSpec" should {
    "load JAR successfully" in {
      // arrange
      val f = fixture
      val jarLoader = TestActorRef(f.props, f.parent.ref, s"$ClassStorage")
      // act
      jarLoader ! StoreJar(f.jarFile)
      // assert
      f.parent.expectMsg(ClassesContainer(_: List[Class[_]]))
    }
 }*/
  //FIXME


}
