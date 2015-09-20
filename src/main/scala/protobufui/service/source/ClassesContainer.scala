package protobufui.service.source

import java.io.File
import java.lang.reflect.Constructor
import java.net.URLClassLoader
import javafx.beans.{InvalidationListener, Observable}
import javafx.collections.{FXCollections, ObservableMap}

import com.google.protobuf.Descriptors.FieldDescriptor.JavaType
import com.google.protobuf.{Message, Parser}

import scala.collection.JavaConverters._


object ClassesContainer extends Observable{

  private val classes: ObservableMap[String, Class[_]] = FXCollections.observableHashMap[String,Class[_]]()

  private var listeners: List[InvalidationListener] = List[InvalidationListener]()

  override def removeListener(listener: InvalidationListener) = {listeners = listeners filterNot( l=>l.equals(listener))}

  override def addListener(listener: InvalidationListener) = {listeners = listeners ::: List(listener)}

  def exists(clazzName: String): Boolean = classes.containsKey(clazzName)

  def putClass(clazz: (String, Class[_])) = {
    classes.put(clazz._1, clazz._2)
    listeners.foreach{listener=>listener.invalidated(this)}
  }

  def getClass(clazzName: String): Class[_] = classes.get(clazzName)

  def getInstanceOf(clazzName: String): Any = {
    val clazz: Class[_] = classes.get(clazzName)
    val constructor: Constructor[_] = clazz.getConstructor()
    constructor.newInstance()
  }

  def getClasses: Iterable[MessageClass] = {
    import scala.collection.JavaConverters._
    classes.values().asScala
      .filter(clazz => clazz.getMethods.filter(x=>x.getName.equals("getDefaultInstance")).length>0)
      .map(MessageClass)
  }

  case class MessageClass(clazz: Class[_]) {
    private val defaultInstance = clazz.getMethod("getDefaultInstance").invoke(null).asInstanceOf[Message]

    def getParser: Parser[_] = defaultInstance.getParserForType

    def getBuilder: Message.Builder = defaultInstance.newBuilderForType()

    def newInstance: Message = defaultInstance.getDefaultInstanceForType

    def getInstanceFilledWithDefaults = {
      val builder = defaultInstance.newBuilderForType()
      builder.fillWithDefaults.build()
    }
  }

  implicit class MessageUtil(builder: Message.Builder) {
    def fillWithDefaults : Message.Builder = {
      builder.getDescriptorForType.getFields.asScala.foreach(field =>
        if (field.getJavaType == JavaType.MESSAGE) {
          val fieldBuilder = builder.getFieldBuilder(field)
          builder.setField(field, fieldBuilder.fillWithDefaults.build())
        }
        else {
          field.getDefaultValue
          builder.setField(field, field.getDefaultValue)
        }
      )
      builder
    }
  }
}
