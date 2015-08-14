package protobufui.gui

import java.io.File
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.TreeView

import ipetoolkit.workspace.{NewWorkspace, WorkspaceEntry, WorkspaceManager}
import protobufui.gui.workspace.RootEntry

/**
 * Created by krever on 8/14/15.
 */
class MainController extends Initializable {

  @FXML
  var workspaceTreeView: TreeView[WorkspaceEntry] = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    workspaceTreeView.setShowRoot(false)
    import Main.eventBus
    val wm = Main.actorSystem.actorOf(WorkspaceManager.props(workspaceTreeView))
    val workspaceDir = new File(System.getProperty("user.home"), ".protobufUI")
    wm ! NewWorkspace(workspaceDir.getAbsolutePath, new RootEntry) //TODO poprawic: 1) jawnie wywolane przez uzytkownika z podana sciezka 2) przez eventBus(teraz nie mozna bo kolejnosc inicjalizacji)
  }

}
