package protobufui.service.source.loaders

import java.io.File
import java.net.URLClassLoader

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.{ClassesContainer, Utils}

case class LoadDirectory(directory: File)

class DirectoryLoader(workspaceRoot: File)
  extends Actor with ActorLogging {
  val urlClassLoader = URLClassLoader.newInstance(Array(workspaceRoot.toURI.toURL))

  override def receive: Receive = {
    case LoadDirectory(directory: File) => {
      Utils.getFileTree(directory)
        .filter(file => file.getName.endsWith(".java"))
        .map(Utils.compile).map(file => (file, file.getAbsolutePath))
        .map((t: (File, String)) => {
        val className: String = t._2.replace(directory.getAbsolutePath, "").replace("/", ".").replace(".java", "")
        className
      })
        .map(className => {
        val clazz: Class[_] = Utils.loadClass(urlClassLoader, className)
        (className, clazz)
      })
        .foreach[Unit]((t: (String, Class[_])) => {
        ClassesContainer.putClass((t._1, t._2))
      }
        )
    }

  }
}
