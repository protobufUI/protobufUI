package protobufui.test.step

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SetSpecsStepEntry extends WorkspaceEntry with TestStep {
  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepSetSpecs.fxml")

  override def run(context: TestStepContext): (TestStepResult, TestStepContext) = {
    Thread.sleep(100); (new TestStepResult(ResultType.Success), context)
  }

  override def name: String = "SetSpecsStepEntry"
}
