package protobufui.service.message

import java.io.File

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import protobufui.service.message.ClassesLoader._
import protobufui.service.message.loaders._

case class Load(file: File)
case class Loaded()
case class Reload()
object ClassesLoader {

  /**
   * Loads recursively workspaceRoot to memory as Class objects from *.class files.
   */
  case class LoadFromWorkspace()

  /**
   * Loads classes from source that might be .proto file, directory or .jar file
   * @param file
   */
  case class Put(file: File)

}

class ClassesLoader(workspaceRoot: File) extends Actor with ActorLogging {

  private val workspaceLoader = context.actorOf(Props(classOf[WorkspaceLoader], workspaceRoot), "workspaceLoader")

  private val directoryLoader = context.actorOf(Props(classOf[DirectoryLoader], workspaceRoot), "directoryLoader")
  private val jarLoader = context.actorOf(Props(classOf[JarLoader]), "jarLoader")
  private val protoLoader = context.actorOf(Props(classOf[ProtoLoader]), "protoLoader")

  override def receive: Receive = {
    case LoadFromWorkspace =>
      workspaceLoader ! Load(null)
    case Put(file) if file.isDirectory =>
      directoryLoader ! Load(file)
    case Put(file) if file.getName.endsWith(".jar") =>
      jarLoader ! Load(file)
    case Put(file) if file.getName.endsWith(".proto") =>
      protoLoader ! Load(file)
    case Loaded =>
      {}
    case x =>
      throw new IllegalArgumentException("Do not know what to load")
  }
}
