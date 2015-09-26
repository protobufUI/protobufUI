package protobufui

import scala.collection.parallel.mutable

object Globals {
  val globals:mutable.ParHashMap[String,String] = new mutable.ParHashMap[String,String]
  def getProperty(key:String):Option[String] = globals.get(key)
  def setProperty(key:String, value:String) = globals += (key->value)
  def deleteProperty(key:String):Option[String] = globals.remove(key)
}
