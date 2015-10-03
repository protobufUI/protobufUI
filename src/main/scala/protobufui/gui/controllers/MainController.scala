package protobufui.gui.controllers

import java.io.File
import java.net.URL
import java.util.ResourceBundle
import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.geometry.Pos
import javafx.scene.control.{Label, TabPane, TreeView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{BorderPane, HBox, StackPane}

import ipetoolkit.details.DetailsTabPaneManager
import ipetoolkit.workspace.WorkspaceManagement.{LoadOrNewWorkspace, SaveWorkspace}
import ipetoolkit.workspace.{WorkspaceEntryView, WorkspaceManager}
import protobufui.Globals
import protobufui.Main
import protobufui.gui.workspace.base.RootEntry


class MainController extends Initializable {

  @FXML
  var workspaceTreeView: TreeView[WorkspaceEntryView] = _

  @FXML
  var expandingResultButton: Label = _

  @FXML
  var expandingResultView: BorderPane = _

  @FXML
  var expandingBar: HBox = _

  @FXML
  var detailsTabPane: TabPane = _

  val workspaceFileName = "workspace.xml"

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    import Main.eventBus
    workspaceTreeView.setShowRoot(false)
    val wm = Main.actorSystem.actorOf(WorkspaceManager.props(workspaceTreeView))
    wm ! LoadOrNewWorkspace(Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + workspaceFileName, RootEntry.createRootEntryWithSubRoots())
    Main.actorSystem.actorOf(DetailsTabPaneManager.props(detailsTabPane))
    initExpandingResultView()

  }

  def initExpandingResultView(): Unit = {
    expandingResultButton onMouseEnteredProperty() setValue new EventHandler[MouseEvent] {
      override def handle(event: MouseEvent): Unit = expandingResultButton.setId("lightShadow")
    }
    expandingResultButton onMouseExitedProperty() setValue new EventHandler[MouseEvent] {
      override def handle(event: MouseEvent): Unit = expandingResultButton.setId("")
    }
    expandingResultButton onMouseClickedProperty() setValue new EventHandler[MouseEvent] {
      override def handle(event: MouseEvent): Unit = toggleResultViewVisible()
    }
    StackPane.setAlignment(expandingResultView, Pos.BOTTOM_CENTER)

    expandingResultView.setDisable(true)
    expandingResultView.setVisible(false)

  }

  def toggleResultViewVisible(): Unit = {
    expandingResultView.setDisable(!expandingResultView.isDisable)
    expandingResultView.setVisible(!expandingResultView.isVisible)
  }


  def saveWorkspace(): Unit = {
    Main.eventBus.publish(SaveWorkspace(Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + workspaceFileName))
  }

}
