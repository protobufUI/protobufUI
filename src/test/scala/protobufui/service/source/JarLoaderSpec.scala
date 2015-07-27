package protobufui.service.source

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import protobufui.service.source.JarLoader.Load

/**
 * Created by pcejrowski on 2015-07-16.
 */
class JarLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

    def this() = this(ActorSystem("JarLoaderSpec"))

    def fixture = new {
      val parent = TestProbe()
      val props = Props(new JarLoader())
      val jarFile = new File(System getProperty ("user.home"), "test.jar")
    }

    "JarLoaderSpec" should {
      "load JAR successfully" in {
        // arrange
        val f = fixture
        val jarLoader = TestActorRef(f.props, f.parent.ref, s"$JarLoader")
        // act
        jarLoader ! Load(f.jarFile)
        // assert
        f.parent.expectMsg(JarReference)
      }

    }
}
