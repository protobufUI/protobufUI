package protobufui.service.source

import java.lang.reflect.Constructor
import java.util.concurrent.ConcurrentHashMap


object ClassesContainer {
  // should be val / how to fix this ?
  var classes: ConcurrentHashMap[String, Class[_]] = new ConcurrentHashMap[String, Class[_]]()

  def putClass(clazz: (String, Class[_])) = classes.put(clazz._1, clazz._2)

  def getClass(className: String): Class[_] = classes.get(className)

  def getInstanceOf(className: String): Any = {
    val clazz: Class[_] = Class.forName(className)
    val constructor: Constructor[_] = clazz.getConstructor(classOf[String])
    constructor.newInstance()
  }
}
