package protobufui.test.step

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType

import scala.concurrent.Future

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SetSpecsStepEntry extends WorkspaceEntry with TestStep {

  def this(name:String){
    this()
    setName(name)
  }

  var propertyValueMap : Map[String, String] = Map()

  override def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    Future.successful((ResultType.Success, context.addProperties(propertyValueMap)))
  }

}
