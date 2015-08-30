package protobufui.service.source

import java.lang.reflect.Constructor
import java.util.concurrent.ConcurrentHashMap

import _root_.test.PbTest
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType
import com.google.protobuf.{Message, Parser}

import scala.collection.JavaConverters._


object ClassesContainer {

  private val classes: ConcurrentHashMap[String, Class[_]] = new ConcurrentHashMap[String, Class[_]]()
  classes.put(classOf[PbTest.Person].getName, classOf[PbTest.Person])

  //FIXME

  def exists(clazzName: String): Boolean = classes.containsKey(clazzName)

  def putClass(clazz: (String, Class[_])) = classes.put(clazz._1, clazz._2)

  def getClass(clazzName: String): Class[_] = classes.get(clazzName)

  def getInstanceOf(clazzName: String): Any = {
    val clazz: Class[_] = Class.forName(clazzName)
    val constructor: Constructor[_] = clazz.getConstructor(classOf[String])
    constructor.newInstance()
  }

  def getClasses: Iterable[MessageClass] = {
    import scala.collection.JavaConverters._
    classes.values().asScala.map(MessageClass)
  }

  //TODO Przydaloby sie cos co powiadomi o nowych klasach...(javafx property, observable list, albo cos takiego)

  case class MessageClass(clazz: Class[_]) {
    private val defaultInstance = clazz.getMethod("getDefaultInstance").invoke(null).asInstanceOf[Message]

    def getParser: Parser[_] = defaultInstance.getParserForType

    def getBuilder: Message.Builder = defaultInstance.newBuilderForType()

    def newInstance: Message = defaultInstance.getDefaultInstanceForType

    def getInstanceFilledWithDefaults = {
      val builder = defaultInstance.newBuilderForType()
      MessageUtil.fillWithDefaults(builder).build()
    }
  }

  //TODO przeniesc stad
  object MessageUtil {
    def fillWithDefaults(builder: Message.Builder): Message.Builder = {
      builder.getDescriptorForType.getFields.asScala.foreach(field =>
        if (field.getJavaType == JavaType.MESSAGE) {
          val fieldBuilder = builder.getFieldBuilder(field)
          builder.setField(field, fillWithDefaults(fieldBuilder).build())
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
