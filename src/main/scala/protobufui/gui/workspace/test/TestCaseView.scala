package protobufui.gui.workspace.test

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, Menu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.TestCaseEntry
import protobufui.test.step.{ScriptStepEntry, SendMessageStepEntry, SetSpecsStepEntry}
import protobufui.util.dialog.Dialog

class TestCaseView(val model: TestCaseEntry) extends WorkspaceEntryView {

  override def contextMenu: Option[ContextMenu] = {
    val newTestStep = new Menu("New")
    val messageStep = new MenuItem("Send message test")
    val setSpecsStep = new MenuItem("Param value set")
    val validateStep = new MenuItem("Validation test")

    val rename = new MenuItem("Rename")
    rename.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        Dialog.rename(nameProperty)
      }
    })
    val delete = new MenuItem("Delete")
    delete.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        model.remove()
      }
    })

    newTestStep.getItems.addAll(messageStep, setSpecsStep, validateStep)

    addEntryOnAction(messageStep, new SendMessageStepEntry("New Send Message Step"))
    addEntryOnAction(setSpecsStep, new SetSpecsStepEntry("New Set Specs Step"))
    addEntryOnAction(validateStep, new ScriptStepEntry("New Script Step"))

    Some(new ContextMenu(newTestStep, rename, delete))
  }

  override def detailsPath = Some("/fxml/testCaseDetails.fxml")

  private def addEntryOnAction(menuItem: MenuItem, entry: => WorkspaceEntry) = menuItem.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent): Unit = {
      addChild(entry)
    }
  })

  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: SendMessageStepEntry => new TestStepView(x, "/fxml/stepMessage.fxml", "SendMessageStep")
    case x: SetSpecsStepEntry => new TestStepView(x, "/fxml/stepSetSpecs.fxml", "SetSpecsStep")
    case x: ScriptStepEntry => new TestStepView(x, "/fxml/stepScript.fxml", "ScriptStep")
  }
}
