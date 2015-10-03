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
  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepSetSpecs.fxml", "SetSpecsStepEntry")

  override def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    Thread.sleep(100)
    Future.successful((ResultType.Success, context))
  }

  override def name: String =  nameProperty.get()
}
