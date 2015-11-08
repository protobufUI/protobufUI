package protobufui.util

import scala.collection.parallel.mutable

object Globals {

  object Keys {
    val workspaceRoot: String = "workspace.root"

  }

  val globals: mutable.ParHashMap[String, String] = new mutable.ParHashMap[String, String]

  //TODO poprawic, not thread safe
  def getProperty(key:String):Option[String] = globals.get(key)
  def setProperty(key:String, value:String) = globals += (key->value)
  def deleteProperty(key:String):Option[String] = globals.remove(key)
}
