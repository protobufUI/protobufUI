package protobufui.service.source.loaders

import java.io.File
import java.nio.file.{Files, StandardCopyOption}

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source._

class DirectoryLoader(workspaceRoot: File) extends Actor with ActorLogging {

  val classLoader = new ClassLoader(workspaceRoot)

  override def receive: Receive = {
    case Load(directory: File) => {
      Utils.getFileTree(directory)
        .filter(file => file.getName.endsWith(".java"))
        .map(Utils.compile)
        .flatMap(getClassFiles(_))
        .map(file => {
        copyClassFilesToWorkspace(directory, file)
        extractClassName(directory, file)
      })
        .foreach(classLoader.load(_))

        context.parent ! Loaded
    }
  }

  def getClassFiles(file: File): Array[File] = {
    file.getParentFile.listFiles()
      .filter(f => {
      // file names are in Outer$Inner$InnerOfInner.class format
      val name = file.getName.replace(".java", "")
      val fileName: String = f.getName
      fileName.startsWith(name) && fileName.endsWith(".class")
    })
  }

  def copyClassFilesToWorkspace(classFile: File, directory: File): Unit = {
    val source = classFile.toPath
    val ending: String = classFile.getAbsolutePath.replace(directory.getAbsolutePath, "")
      val destinationFile: File = new File(workspaceRoot, ending)
      destinationFile.mkdirs()
      val destination = destinationFile.toPath
      Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
  }

  def extractClassName(file: File, rootDirectory: File): String = {
    file.getAbsolutePath
        .replace(rootDirectory.getAbsolutePath, "")
        .replace("\\", ".")
        .replace(".class", "").substring(1)
  }
}
