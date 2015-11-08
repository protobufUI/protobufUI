package protobufui.service.message

import javafx.beans.{Observable, InvalidationListener}
import javafx.collections.{FXCollections, ObservableMap}

import com.google.protobuf.Descriptors.FieldDescriptor.JavaType
import com.google.protobuf.{Message, MessageLite}
import protobufui.model.MessageClass
import scala.collection.JavaConverters._

/**
 * Created by krever on 11/8/15.
 */
object ClassesContainer extends Observable{

  private val classes: ObservableMap[String, MessageClass] = FXCollections.observableHashMap[String,MessageClass]()

  private var listeners: List[InvalidationListener] = List[InvalidationListener]()

  override def removeListener(listener: InvalidationListener) = {listeners = listeners filterNot( l=>l.equals(listener))}

  override def addListener(listener: InvalidationListener) = {listeners = listeners ::: List(listener)}

  def exists(clazzName: String): Boolean = classes.containsKey(clazzName)

  def putClass(clazz: (String, Class[_])) = {
    if(classOf[MessageLite].isAssignableFrom(clazz._2)) {
      classes.put(clazz._1, new MessageClass(clazz._2))
      listeners.foreach { listener => listener.invalidated(this) }
    }
  }

  def putClassWOInvalidation(clazz: (String, Class[_])) = {
    if (classOf[MessageLite].isAssignableFrom(clazz._2)) {
      classes.put(clazz._1, new MessageClass(clazz._2))
    }
  }


  def getClass(clazzName: String): MessageClass = classes.get(clazzName)

  def getInstanceOf(clazzName: String): Any = {
    val clazz: MessageClass = classes.get(clazzName)
    clazz.newInstance
  }

  def getClasses: Iterable[MessageClass] = {
    import scala.collection.JavaConverters._
    classes.values().asScala
  }
}
