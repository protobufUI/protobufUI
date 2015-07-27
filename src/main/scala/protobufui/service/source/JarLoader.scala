package protobufui.service.source

import java.io.{File, FileInputStream, InputStream, PrintWriter}
import java.net.{URI, URL, URLClassLoader}
import java.util.jar.JarInputStream
import javax.tools.ToolProvider

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.JarLoader.Load

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * Created by pcejrowski on 2015-07-16.
 */
object JarLoader {

  case class Load(file: File)

}

class JarLoader() extends Actor with ActorLogging {

  def receive = {
    case Load(file) =>
      val jarInputStream = new JarInputStream(new FileInputStream(file))
      val classLoader = URLClassLoader.newInstance(Array(file.toURL), getClass.getClassLoader)
      val userHome = System.getProperty("user.home")
      val storage = new File(userHome, "\\.protobufUI/compiledClasses/")
      val compiler = ToolProvider.getSystemJavaCompiler();
      val fileURL = file.toURI.toString
      storage.getParentFile().mkdirs()


      // dupa dupa dupa cycki
      // fixme: move some functionality to JarReference class
      // todo: refactor
      // todo: use JiMCy
      val loading = Try {
        val loadedClasses = Stream.continually(jarInputStream.getNextEntry).takeWhile(_ != null)
          .map(_.getName)
          .filter(_.endsWith(".java"))
          .map(relative => (new URI("jar:" + fileURL + "!/" + relative).toURL, relative))
          .map((t: (URL, String)) => (t._1.openConnection.getInputStream, t._2))
          .map((t: (InputStream, String)) => (Source.fromInputStream(t._1).getLines().mkString, t._2))
          .map((t: (String, String)) => {
          val sourceFile: String = storage + "/" + t._2
          new File(sourceFile).getParentFile().mkdirs()
          val writer = new PrintWriter(sourceFile)
          writer.println(t._1)
          (sourceFile, t._2)
        }
          )
          .map((t: (String, String)) => {
          compiler.run(null, null, null, t._1)
          t._2
        }
          )
        val x = loadedClasses
          .map(Class.forName(_, true, classLoader))
          .toList
        new JarReference(x)
      }

      loading match {
        case Success(value) => context.parent ! value
        case Failure(e) => log.error(e, "Java Archive could not be loaded.")
      }
    case _ =>
      println("Something went wrong")
  }
}
