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
        copyToWorkspace(file,directory)
        extractClassName(file,directory)
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

  def copyToWorkspace(file: File, directory: File): Unit = {
    val sourcePath = file.toPath
    val relativePath: String = file.getAbsolutePath.replace(directory.getAbsolutePath, "")
    val destinationFile: File = new File(workspaceRoot, relativePath)
    destinationFile.mkdirs()
    val destinationPath = destinationFile.toPath
    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
  }

  def extractClassName(file: File, rootDirectory: File): String = {
    file.getAbsolutePath
        .replace(rootDirectory.getAbsolutePath, "")
        .replace("\\", ".")
        .replace(".class", "").substring(1)
  }
}
