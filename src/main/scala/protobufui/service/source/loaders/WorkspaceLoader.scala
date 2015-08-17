package protobufui.service.source.loaders

import java.io.File
import java.nio.file.Files

import akka.actor.{ActorLogging, Actor}
import protobufui.service.source._

class WorkspaceLoader(workspaceRoot: File) extends Actor with ActorLogging {
  val classLoader = new ClassLoader(workspaceRoot)
  override def receive: Receive = {
        case Load(_) => {
          Utils.getFileTree(workspaceRoot)
            .filter(file => file.getName.endsWith(".class"))
            .map(_.getAbsolutePath)
            .map(_.replace(workspaceRoot.getAbsolutePath,""))
            .map(_.replace(File.separator,".").substring(1).replace(".class",""))
            .foreach(classLoader.loadAndStore(_))

            context.parent ! Loaded
          }
      }
}
