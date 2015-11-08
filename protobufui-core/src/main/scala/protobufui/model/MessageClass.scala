package protobufui.model

import com.google.protobuf.Descriptors.FieldDescriptor.JavaType
import com.google.protobuf.{MessageLite, UnknownFieldSet, Message}
import scala.collection.JavaConverters._

/**
 * Created by krever on 11/8/15.
 */
case class MessageClass(clazz: Class[_]) extends MessageLiteClass(clazz: Class[_]){
  private val defaultInstance = defaultInstanceLite.asInstanceOf[Message]

  def getBuilder: Message.Builder = defaultInstance.newBuilderForType()

  def newInstance: Message = defaultInstance.getDefaultInstanceForType

  def getInstanceFilledWithDefaults = {
    val builder = defaultInstance.newBuilderForType()
    fillWithDefaults(builder).build()
  }
  override def toString:String = clazz.getName

  private def fillWithDefaults(builder: Message.Builder) : Message.Builder = {
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

class MessageLiteClass(clazzLite: Class[_]) {
  val defaultInstanceLite: MessageLite = clazzLite.getMethod("getDefaultInstance").invoke(null).asInstanceOf[MessageLite]
  def getParser = defaultInstanceLite.getParserForType

}

