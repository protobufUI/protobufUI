package protobufui.gui.controllers

import java.net.URL
import java.util
import java.util.ResourceBundle
import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{ListView, TextArea}
import javafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination, KeyEvent}

import ipetoolkit.details.DetailsController
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.Main
import protobufui.model.steps.ScriptStepEntry
import protobufui.service.script.ScalaScriptingCtx
import protobufui.service.testengine.TestStepContext
import protobufui.service.testengine.runner.step.TestStepRunnerFactory

import scala.collection.JavaConverters._

/**
 * Created by humblehound on 26.09.15.
 */
//TODO wyswietlic output ze skryptu w jakiejs kontrolce
class StriptStepController extends Initializable with DetailsController {

  @FXML
  var scriptArea:TextArea = _
  @FXML
  var completionList: ListView[String] = _

  lazy val scriptCtx = new ScalaScriptingCtx()

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    val ctrlSpaceKeyComb = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN)
    scriptArea.setOnKeyPressed(new EventHandler[KeyEvent] {
      override def handle(event: KeyEvent): Unit = {
        if (ctrlSpaceKeyComb.`match`(event)) {
          completeScript()
        }
      }
    })
  }

  def scriptStep = model.asInstanceOf[ScriptStepEntry]

  override def setModel(workspaceEntry: WorkspaceEntry): Unit = {
    super.setModel(workspaceEntry)
    scriptArea.setText(scriptStep.script)
  }

  @FXML
  def saveScript(): Unit = {
    scriptStep.script = scriptArea.getText
  }

  @FXML
  def runScript(): Unit = {
    val oldScript = scriptStep.script
    scriptStep.script = scriptArea.getText
    TestStepRunnerFactory.createRunner(scriptStep).run(new TestStepContext()(Main.actorSystem))
    scriptStep.script = oldScript
  }

  private def completeScript(): Unit ={
    scriptCtx.engine.reset()
    scriptCtx.engine.bind("ctx", new TestStepContext()(Main.actorSystem))

    val lines = scriptArea.getText
    val candidates = new util.ArrayList[CharSequence]()
    scriptCtx.completion.complete(lines, scriptArea.getCaretPosition, candidates)
    completionList.getItems.setAll(candidates.asScala.map(_.toString): _*)
  }



}
