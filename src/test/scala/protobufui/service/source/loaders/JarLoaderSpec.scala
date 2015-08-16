package protobufui.service.source.loaders

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike, _}
import protobufui.service.source.ClassesLoader.Put
import protobufui.service.source.Load

import scala.concurrent.duration.Duration

class JarLoaderSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with ImplicitSender {

    def this() = this(ActorSystem("JarLoaderSpec"))

    def fixture = new {
      val parent = TestProbe()
      val props = Props(classOf[JarLoader])
      val jarFile = new File("src/test/resources/protobufui/service/source/testee.jar")
    }

  "JarLoaderSpec" should {
    "jar is stored in temp directory" in {
      // arrange
      val f = fixture
      val jarLoader = TestActorRef(f.props, f.parent.ref, "JarLoaderSpec")
      // act
      jarLoader ! Load(f.jarFile)
      val receivedMsg: AnyRef = f.parent.receiveOne(Duration(2000, "millis"))
      val msg = receivedMsg.asInstanceOf[Put]
      val extractedFileName: String = msg.file.listFiles()
        .filter(
          file => file.getName.equals("test")
        )
        .head.listFiles()
        .head.getName
      // assert
      Assertions.assert("PersonTest.java".equals(extractedFileName))
      }

    }
}
