package protobufui.service.testengine

import akka.actor.ActorSystem
import com.google.protobuf.MessageLite


case class TestStepContext(stepResponses: Map[String, MessageLite] = Map(), propertyValueMap : Map[String, String] = Map())(implicit val actorSystem: ActorSystem) {

  def addResponse(s: String, lite: MessageLite): TestStepContext = this.copy(stepResponses = stepResponses + (s -> lite))(actorSystem)

  def addProperties(propertyValueMap : Map[String, String]) = this.copy(propertyValueMap = propertyValueMap++propertyValueMap)
}

