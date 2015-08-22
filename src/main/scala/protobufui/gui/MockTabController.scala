package protobufui.gui

import javafx.fxml.FXML
import javafx.scene.control.TextField

import protobufui.gui.workspace.MockEntry


class MockTabController {

  var workspaceEntry: MockEntry = _

  @FXML
  var nameTextField: TextField = _

  def setWorkspaceEntry(entry: MockEntry) = {
    workspaceEntry = entry
    nameTextField.textProperty().bindBidirectional(workspaceEntry.nameProperty)
  }

}
