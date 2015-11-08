package protobufui.service.testengine.runner.step

import protobufui.model.steps.ScriptStepEntry
import protobufui.service.script.ScalaScriptingCtx
import protobufui.service.testengine.ResultType.ResultType
import protobufui.service.testengine.{ResultType, TestStepContext}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * Created by krever on 11/8/15.
 */
class ScriptStepRunner(step: ScriptStepEntry) extends TestStepRunner {

  def run(context: TestStepContext): Future[(ResultType, TestStepContext)] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Future[(ResultType, TestStepContext)] {
      val scripting = new ScalaScriptingCtx
      scripting.engine.bind("ctx", context)

      val resultTry = Try(scripting.engine.eval(step.script))

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
