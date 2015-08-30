package protobufui.gui

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
import ipetoolkit.workspace.WorkspaceManagement.NewWorkspace
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceManager}
import protobufui.gui.workspace.RootEntry


class MainController extends Initializable {

  @FXML
  var workspaceTreeView: TreeView[WorkspaceEntry] = _

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
    val workspaceDir = new File(System.getProperty("user.home"), ".protobufUI")
    wm ! NewWorkspace(workspaceDir.getAbsolutePath, new RootEntry) //TODO poprawic: 1) jawnie wywolane przez uzytkownika z podana sciezka 2) przez eventBus(teraz nie mozna bo kolejnosc inicjalizacji)

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
    StackPane.setAlignment(expandingResultView, Pos.BOTTOM_CENTER);

    expandingResultView.setDisable(true)
    expandingResultView.setVisible(false)

  }

  def toggleResultViewVisible(): Unit = {
    expandingResultView.setDisable(!expandingResultView.isDisable)
    expandingResultView.setVisible(!expandingResultView.isVisible)
  }
}
