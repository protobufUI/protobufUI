package protobufui.test

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestSuiteView

@XmlRootElement
class TestSuite extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new TestSuiteView(this)
}
