package protobufui.test.step

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView
import protobufui.test.ResultType
import protobufui.test.ResultType.ResultType

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class ValidateStepEntry extends WorkspaceEntry with TestStep {

  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepValidate.fxml", "ValidateStepEntry")
  
  override def name: String = nameProperty.get()

  override def run(context: TestStepContext): (ResultType, TestStepContext) = (ResultType.Success, context)
}
