package protobufui.test


case class TestStepDefinition(

                               )

object TestStepType extends Enumeration {
  type TestStepType = Value
  val SendMessage, SetSpecs, ValidateResults = Value
}
