package protobufui.test

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestCaseView
import protobufui.test.step.{TestStep, TestStepContext, TestStepResult}

@XmlRootElement
class TestCaseEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestCaseView(this)

  def run() = {
    //TODO asynchronicznie z aktulizowanie wynikow na bierzaco. Teraz jak odpalimy testcase to nam caly program zwiesi
    val context = new TestStepContext
    val testSteps = gatherTestSteps()

    testSteps.foldLeft((List[TestStepResult](), context)) {
      case ((results, ctx), step) =>
        val (result, newCtx) = step.run(ctx)
        (results :+ result, newCtx)
    }

  }

  private def gatherTestSteps(): List[TestStep] = children.collect {
    case t: TestStep => t
  }

}
