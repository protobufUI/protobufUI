package protobufui.service.source

import java.io.{File, FileInputStream, InputStream}
import java.net.{URI, URL, URLClassLoader}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, OpenOption, StandardOpenOption}
import java.util.jar.JarInputStream
import javax.tools.ToolProvider

import akka.actor.{Actor, ActorLogging}
import protobufui.service.source.ClassStorage.{StoreClassesFromDirectory, StoreJar, StoreProtoClasses}

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * Created by pcejrowski on 2015-07-16.
 */
object ClassStorage {

  case class StoreJar(jarFile: File)

  case class StoreClassesFromDirectory(directory: File)

  case class StoreProtoClasses(protoFile: File)

  class CompilationEntry(name: String, code: String)


}

class ClassStorage(workspaceStorage: File)
  extends Actor with ActorLogging {

  val urlClassLoader = URLClassLoader.newInstance(Array(workspaceStorage.toURI.toURL))

  def this(workspaceStorage: File) {
    this(workspaceStorage)
    workspaceStorage getParentFile() mkdirs
  }

  def receive = {
    case StoreJar(file) =>
      val loading = Try {
        val loadedClasses =
          saveJarAsSources(file)
            .map(compile)
            .map(loadClass)
            .toList
        new JarReference(loadedClasses)
      }
      loading match {
        case Success(value) => context.parent ! value // message: "Hey !, you can reload the classes tree because it might have changed"
        case Failure(e) => log.error(e, "Java Archive could not be loaded.")
      }
    case StoreClassesFromDirectory(directory) =>

    case StoreProtoClasses(protoFile) =>

    case _ =>
      println("Something went wrong")
  }

  def saveJarAsSources(jarFile: File): Stream[(File, String)] = {
    val fileURL = jarFile.toURI.toString
    val jarInputStream = new JarInputStream(new FileInputStream(jarFile))
    Stream.continually(jarInputStream.getNextEntry).takeWhile(_ != null)
      .map(_.getName)
      .filter(_.endsWith(".java"))
      .map(pathInJar => (new URI("jar:" + fileURL + "!/" + pathInJar).toURL, pathInJar))
      .map((t: (URL, String)) => (t._1.openConnection.getInputStream, t._2))
      .map((t: (InputStream, String)) => (Source.fromInputStream(t._1).getLines().mkString("\n"), t._2)) // fu#!*ng \n that costs me 3h
      .map((t: (String, String)) => {
      val fileWithSources: File = new File(workspaceStorage + "/" + t._2)
      createFileWithContent(fileWithSources, t._1)
      (fileWithSources, t._2)
    })
      .map((t: (File, String)) => {
      (t._1, t._2 replace(".java", "") replace('/', '.'))
    })
  }

  def createFileWithContent(file: File, content: String): Unit = {
    // refactor into some implicit File extension !?
    file.getParentFile().mkdirs
    val openOption: OpenOption = StandardOpenOption.CREATE
    Files.write(file.toPath, content.getBytes(StandardCharsets.UTF_8), openOption)
  }

  def compile: ((File, String)) => String = {
    (t: (File, String)) => {
      val javac = ToolProvider.getSystemJavaCompiler
      javac.run(null, null, null, t._1.getPath)
      t._2
    }
  }

  def loadClass(className: String): Class[_] = {
    Class.forName(className, true, urlClassLoader)
  }


}
