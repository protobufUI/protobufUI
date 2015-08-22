package protobufui.gui

import java.io.File
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{TabPane, TreeView}

import ipetoolkit.details.DetailsTabPaneManager
import ipetoolkit.workspace.WorkspaceManagement.NewWorkspace
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceManager}
import protobufui.gui.workspace.RootEntry


class MainController extends Initializable {

  @FXML
  var workspaceTreeView: TreeView[WorkspaceEntry] = _

  @FXML
  var detailsTabPane: TabPane = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    workspaceTreeView.setShowRoot(false)
    import Main.eventBus
    val wm = Main.actorSystem.actorOf(WorkspaceManager.props(workspaceTreeView))
    val workspaceDir = new File(System.getProperty("user.home"), ".protobufUI")
    wm ! NewWorkspace(workspaceDir.getAbsolutePath, new RootEntry) //TODO poprawic: 1) jawnie wywolane przez uzytkownika z podana sciezka 2) przez eventBus(teraz nie mozna bo kolejnosc inicjalizacji)

    Main.actorSystem.actorOf(DetailsTabPaneManager.props(detailsTabPane))

  }

}
