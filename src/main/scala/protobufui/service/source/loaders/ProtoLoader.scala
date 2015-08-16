package protobufui.service.source.loaders

import java.io.File

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.ClassesLoader.Put
import protobufui.service.source.Load
import scala.sys.process._


class ProtoLoader extends Actor with ActorLogging {

  val  uuid = java.util.UUID.randomUUID toString
  val temporaryStorage: File = new File(System.getProperty("java.io.tmpdir"), "protobuf/" + uuid)
  val outputFile:String = temporaryStorage.getAbsolutePath

  override def receive: Receive = {
    case Load(file) => {
      val parentAbsolutePath = file.getParentFile.getAbsolutePath
      val absolutePath= file.getAbsolutePath
      temporaryStorage.mkdirs()
      s"protoc -I=$parentAbsolutePath --java_out=$outputFile $absolutePath" ! // Amazing !!! I<3Scala for this line.

      context.parent ! Put(temporaryStorage)
    }
  }
}
