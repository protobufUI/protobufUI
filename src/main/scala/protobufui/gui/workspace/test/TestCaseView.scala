package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, Menu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.TestCaseEntry
import protobufui.test.step.{SendMessageStepEntry, SetSpecsStepEntry, ValidateStepEntry}
import protobufui.util.dialog.Dialog

class TestCaseView(workspaceEntry: TestCaseEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Test Case")

  override def model = workspaceEntry

  override def contextMenu: Option[ContextMenu] = {

    val newTestStep = new Menu("New")
    val messageStep = new MenuItem("Send message test")
    val setSpecsStep = new MenuItem("Param value set")
    val validateStep = new MenuItem("Validation test")

    val rename = new MenuItem("Rename")
    rename.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        Dialog.rename(nameProperty);
      }
    })
    val delete = new MenuItem("Delete")
    delete.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        model.delete()
      }
    })

    newTestStep.getItems.addAll(messageStep, setSpecsStep, validateStep)

    addEntryOnAction(messageStep, new SendMessageStepEntry)
    addEntryOnAction(setSpecsStep, new SetSpecsStepEntry)
    addEntryOnAction(validateStep, new ValidateStepEntry)

    Some(new ContextMenu(newTestStep, rename, delete))
  }

  override def detailsPath: String = "/fxml/testCaseDetails.fxml"

  private def addEntryOnAction(menuItem: MenuItem, entry: => WorkspaceEntry) = menuItem.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent): Unit = {
      addWorkSpaceEntry(entry)
    }
  })
}
