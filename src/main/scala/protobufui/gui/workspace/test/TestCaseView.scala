package protobufui.gui.workspace.test

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{ContextMenu, Menu, MenuItem}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.test.SendMessageStep

class TestCaseView(workspaceEntry: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Test Case")

  override def model = workspaceEntry

  override def contextMenu: Option[ContextMenu] = {
    val newTestStep = new Menu("New")
    val messageStep = new MenuItem("Send message test")
    val setSpecsStep = new MenuItem("Param value set")
    val validateStep = new MenuItem("Validation test")

    newTestStep.getItems.addAll(messageStep, setSpecsStep, validateStep)

    messageStep.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        addWorkSpaceEntry(new SendMessageStep)
      }
    })
    setSpecsStep.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        //addWorkSpaceEntry(new TestStep(TestStepType.Params))
      }
    })
    validateStep.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        //addWorkSpaceEntry(new TestStep(TestStepType.Validation))
      }
    })
    Some(new ContextMenu(newTestStep))
  }

}
