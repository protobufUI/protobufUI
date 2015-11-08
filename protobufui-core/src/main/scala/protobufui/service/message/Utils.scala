package protobufui.service.message

import java.io.File
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, OpenOption, StandardOpenOption}
import javax.tools.ToolProvider

object Utils {
  def compile(file: File): File = {
    try {
      val javac = ToolProvider.getSystemJavaCompiler
      javac.run(null, null, null, file.getPath)
    }catch{
      case e:NullPointerException =>
        throw new RuntimeException("Check if you are using JDK. JRE does not support runtime compilation", e)
    }
    file
  }

  implicit def createFileWithContent(file: File, content: String): Unit = {
    file.getParentFile.mkdirs
    val openOption: OpenOption = StandardOpenOption.CREATE
    Files.write(file.toPath, content.getBytes(StandardCharsets.UTF_8), openOption)
  }

  def getFileTree(file: File): Stream[File] =
    file #:: (if (file.isDirectory) file.listFiles().toStream.flatMap(getFileTree)
    else Stream.empty)
}
class ClassLoader(rootFile:File){
  private val rootFileUrl = new File(rootFile,"classes").toURI.toURL
  val urlClassLoader = URLClassLoader.newInstance(Array(rootFileUrl))
  def loadAndStore(className: String) = {
    val clazz: Class[_] =  urlClassLoader.loadClass(className)
    ClassesContainer.putClass(className, clazz)
  }
  def loadAndStoreWOInvalidation(className: String) = {
    val clazz: Class[_] =  urlClassLoader.loadClass(className)
    ClassesContainer.putClassWOInvalidation(className, clazz)
  }
}
