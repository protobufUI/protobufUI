package protobufui.test.step

import ipetoolkit.workspace.{WorkspaceEntryView, WorkspaceEntry}
import protobufui.gui.workspace.test.TestStepView

/**
 * Created by humblehound on 26.09.15.
 */
class SetSpecsStep extends WorkspaceEntry{
  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepSetSpecs.fxml")
}
