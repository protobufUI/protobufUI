package protobufui.service.script


import jline.console.completer.{ArgumentCompleter, Completer}

import scala.collection.JavaConverters._
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.Completion.{Candidates, ScalaCompleter}
import scala.tools.nsc.interpreter.jline.JLineDelimiter
import scala.tools.nsc.interpreter.{IMain, JLineCompletion}

/**
 * Created by krever on 9/5/15.
 */
class ScalaScriptingCtx {
  private val settings = new Settings()
  settings.usejavacp.value = true
  settings.embeddedDefaults[ScalaScriptingCtx]
  val engine = new IMain(settings)

  val completion = new ArgumentCompleter(new JLineDelimiter, List(scalaToJline(new JLineCompletion(engine).completer())):_*)
  completion.setStrict(false)

  private def scalaToJline(tc: ScalaCompleter): Completer = new Completer {
    def complete(_buf: String, cursor: Int, candidates: java.util.List[CharSequence]): Int = {
      val buf = if (_buf == null) "" else _buf
      val Candidates(newCursor, newCandidates) = tc.complete(buf, cursor)
      newCandidates foreach (candidates add _)
      newCursor
    }
  }


}
