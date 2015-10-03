package protobufui.test

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestCaseView
import protobufui.test.step.TestStep

@XmlRootElement
class TestCaseEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestCaseView(this)


  def getTestSteps: List[TestStep] = children.collect { case t: TestStep => t }

}
