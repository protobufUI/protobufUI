package protobufui.service.source

import java.io.File

import akka.actor.{Actor, ActorLogging, Props}
import protobufui.service.source.loaders._

import scala.reflect.io.Directory

/**
 * Loads recursively workspaceRoot to memory as Class objects from *.class files.
 */
case class LoadWorkspace()

/**
 * Put classes defined in the jarFile to both memory as Class objects and
 * saves compiled classes to workspaceRoot directory.
 * @param jarFile - Java Archive containing classes to be loaded(not compiled, as *.java)
 */
case class PutJar(jarFile: File)

/**
 * Put classes defined in *.java files stored in directory to memory as Class objects and
 * saves compiled classes to workspaceRoot directory.
 * @param directory - contains *.java files recursively
 */
case class PutJavaFilesFromDirectory(directory: File)

/**
 * Compiles Proto schema and loads generated files to memory as Class objects and
 * saves compiled classes to workspaceRoot directory.
 * @param protoFile - file *.proto that contains Protocol Buffer definition
 */
case class PutProto(protoFile: File)

/**
 * Allows to load classes from not implicitly defined source.
 * @param file
 */
case class Put(file: File)

class ClassesLoader(workspaceRoot: File)
  extends Actor with ActorLogging {


  private val workspaceLoader = context.actorOf(Props(new WorkspaceLoader(workspaceRoot)), "workspaceLoader")

  private val directoryLoader = context.actorOf(Props(new DirectoryLoader(workspaceRoot)), "directoryLoader")
  private val jarLoader = context.actorOf(Props(new JarLoader), "jarLoader")
  private val protoLoader = context.actorOf(Props(new ProtoLoader), "protoLoader")

  override def receive: Receive = {
    case LoadWorkspace =>
      workspaceLoader ! LoadClasses
    case PutJavaFilesFromDirectory(directory) =>
      directoryLoader ! LoadDirectory(directory)
    case PutJar(jarFile) =>
      jarLoader ! LoadJar(jarFile)
    case PutProto(protoFile) =>
      protoLoader ! LoadProto(protoFile)
    case Put(file) => {
      file match {
        case directory: Directory =>
          self ! PutJavaFilesFromDirectory(directory)
        case file: File =>
          val fileName: String = file.getName
          if (fileName.endsWith(".jar"))
            self ! PutJar(file)
          else if (fileName.endsWith(".proto"))
            self ! PutProto(file)
          else
            throw new IllegalArgumentException("File has wrong extension")
      }
    }
  }
}
