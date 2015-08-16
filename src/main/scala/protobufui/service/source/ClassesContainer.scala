package protobufui.service.source

import java.lang.reflect.Constructor
import java.util.concurrent.ConcurrentHashMap


object ClassesContainer {

  private val classes: ConcurrentHashMap[String, Class[_]] = new ConcurrentHashMap[String, Class[_]]()

  def exists(clazzName: String): Boolean = classes.containsKey(clazzName)

  def putClass(clazz: (String, Class[_])) = classes.put(clazz._1, clazz._2)

  def getClass(clazzName: String): Class[_] = classes.get(clazzName)

  def getInstanceOf(clazzName: String): Any = {
    val clazz: Class[_] = Class.forName(clazzName)
    val constructor: Constructor[_] = clazz.getConstructor(classOf[String])
    constructor.newInstance()
  }
}
