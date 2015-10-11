package protobufui.test.step

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView
import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType

import scala.concurrent.Future

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SetSpecsStepEntry extends WorkspaceEntry with TestStep {

  var propertyValueMap : Map[String, String] = Map()

  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepSetSpecs.fxml", "SetSpecsStepEntry")

  override def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    Future.successful((ResultType.Success, context.addProperties(propertyValueMap)))
  }

  override def name: String =  nameProperty.get()
}
