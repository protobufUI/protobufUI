package protobufui.service.source.loaders

import java.io.File
import java.nio.file.{Files, StandardCopyOption}
import akka.actor.{Actor, ActorLogging}
import protobufui.service.source._

class DirectoryLoader(workspaceRoot: File)
  extends Actor with ActorLogging {
val classLoader = new ClassLoader(workspaceRoot)

  override def receive: Receive = {
    case Load(directory: File) => {
      Utils.getFileTree(directory)
        .filter(file => file.getName.endsWith(".java"))
        .map(Utils.compile)
        .flatMap(file => {
        copyFilesToWorkspace(directory, file)
        extractClassNames(directory, file)
      })
        .foreach(classLoader.loadClass(_))

        context.parent ! Loaded
    }
  }

  def copyFilesToWorkspace(directory: File, javaFile: File): Unit = {
    javaFile.getParentFile.listFiles()
      .map(
        file => (file, javaFile.getName.replace(".java", ""))
      )
      .filter((f: (File, String)) => {
      val name: String = f._1.getName
      name.startsWith(f._2) && name.endsWith(".class")
    }
      ).foreach((f: (File, String)) => {
      val fullPath = f._1.getAbsolutePath
      val source = f._1.toPath
      val ending: String = f._1.getAbsolutePath.replace(directory.getAbsolutePath, "")
      val destinationFile: File = new File(workspaceRoot, ending)
      destinationFile.mkdirs()
      val destination = destinationFile.toPath
      Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
    }
      )
  }

  def extractClassNames(rootDirectory: File, file: File): Array[String] = {
    file.getParentFile.listFiles()
        .filter(file => {
        val name: String = file.getName
        name.startsWith(file.getName.replace(".java","")) && name.endsWith(".class")
      })
      .map(file=>file.getAbsolutePath
        .replace(rootDirectory.getAbsolutePath, "")
        .replace("\\", ".")
        .replace(".class", "").substring(1)
      )
  }
}
