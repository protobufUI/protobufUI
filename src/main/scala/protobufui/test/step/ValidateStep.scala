package protobufui.test.step

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView

/**
 * Created by humblehound on 26.09.15.
 */
class ValidateStep extends WorkspaceEntry{

  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepValidate.fxml")
}
