package protobufui.gui.controllers

import java.io.File
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Label, TabPane, TreeView}
import javafx.scene.layout.{BorderPane, HBox}

import ipetoolkit.details.DetailsTabPaneManager
import ipetoolkit.workspace.WorkspaceManagement.{LoadOrNewWorkspace, SaveWorkspace}
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView, WorkspaceManager}
import protobufui.gui.Main
import protobufui.gui.workspace.RootView
import protobufui.model.RootEntry
import protobufui.util.Globals


class MainController extends Initializable {

  val workspaceFileName = "workspace.xml"
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

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    workspaceTreeView.setShowRoot(false)
    import Main.eventBus
    val wm = Main.actorSystem.actorOf(WorkspaceManager.props(workspaceTreeView))
    var result = wm ! LoadOrNewWorkspace(workspaceFileDir, RootEntry.createRootEntryWithSubRoots(), (model: WorkspaceEntry) => new RootView(model))
    Main.actorSystem.actorOf(DetailsTabPaneManager.props(detailsTabPane))
    initExpandingResultView()

  }

  def workspaceFileDir = Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + workspaceFileName

  def initExpandingResultView(): Unit = {
    //    expandingResultButton onMouseEnteredProperty() setValue new EventHandler[MouseEvent] {
    //      override def handle(event: MouseEvent): Unit = expandingResultButton.setId("lightShadow")
    //    }
    //    expandingResultButton onMouseExitedProperty() setValue new EventHandler[MouseEvent] {
    //      override def handle(event: MouseEvent): Unit = expandingResultButton.setId("")
    //    }
    //    expandingResultButton onMouseClickedProperty() setValue new EventHandler[MouseEvent] {
    //      override def handle(event: MouseEvent): Unit = toggleResultViewVisible()
    //    }
    //    StackPane.setAlignment(expandingResultView, Pos.BOTTOM_CENTER)
    //
    //    expandingResultView.setDisable(true)
    //    expandingResultView.setVisible(false)

  }

  def toggleResultViewVisible(): Unit = {
    //    expandingResultView.setDisable(!expandingResultView.isDisable)
    //    expandingResultView.setVisible(!expandingResultView.isVisible)
  }


  def saveWorkspace(): Unit = {
    Main.eventBus.publish(SaveWorkspace(Globals.getProperty(Globals.Keys.workspaceRoot).get + File.separator + workspaceFileName))
  }

}
