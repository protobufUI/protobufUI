package protobufui.service.source

import java.io.File
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, OpenOption, StandardOpenOption}
import javax.tools.ToolProvider

object Utils {
  def loadClass(classLoader: URLClassLoader, className: String): Class[_] = {
    Class.forName(className, true, classLoader)
  }

  def compile(file: File): File = {
    val javac = ToolProvider.getSystemJavaCompiler
    javac.run(null, null, null, file.getPath)
    file
  }

  // fixme how to make it works for as File class extension
  implicit def createFileWithContent(file: File, content: String): Unit = {
    file.getParentFile().mkdirs
    val openOption: OpenOption = StandardOpenOption.CREATE
    Files.write(file.toPath, content.getBytes(StandardCharsets.UTF_8), openOption)
  }

  def getFileTree(file: File): Stream[File] =
    file #:: (if (file.isDirectory) file.listFiles().toStream.flatMap(getFileTree)
    else Stream.empty)
}
