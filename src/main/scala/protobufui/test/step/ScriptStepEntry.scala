package protobufui.test.step

import javax.xml.bind.annotation.{XmlElement, XmlRootElement}

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView
import protobufui.service.script.ScalaScriptingCtx
import protobufui.test.ResultType
import protobufui.test.ResultType._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class ScriptStepEntry extends WorkspaceEntry with TestStep {
  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepScript.fxml", "ValidateStepEntry")

  @XmlElement
  var script = ""

  override def name: String = nameProperty.get()

  override def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Future[(ResultType, TestStepContext)] {
      val scripting = new ScalaScriptingCtx
      scripting.engine.bind("ctx", context)

      val resultTry = Try(scripting.engine.eval(script))

      resultTry match {
        case Success(result: TestStepContext) => (ResultType.Success, result)
        case Success(null) => (ResultType.Success, context)
        case Success(x) =>
          Console.out.println("Wrong result type from script. Valid Types are TestStepContext and Unit")
          (ResultType.Failure, context)
        case Failure(e) => (ResultType.Failure, context)
      }

    }
  }
}
