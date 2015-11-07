package protobufui.gui.workspace.base

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.mock.{MocksRootEntry, MocksRootView}
import protobufui.test.TestSuite

@XmlRootElement
class RootEntry extends WorkspaceEntry {

  setName("Root")

  def getTests: List[TestSuite] = children.collectFirst{case x: TestsRootEntry => x}.get.children.collect{case x: TestSuite => x}

}

object RootEntry {
  def createRootEntryWithSubRoots(): RootEntry = {
    val root = new RootEntry
    root.addChild(new MessagesRootEntry)
    root.addChild(new MocksRootEntry)
    root.addChild(new TestsRootEntry)
    root
  }
}

class RootEntryView(val model: WorkspaceEntry) extends WorkspaceEntryView {
  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: MessagesRootEntry => new MessagesRootView(x)
    case x: MocksRootEntry => new MocksRootView(x)
    case x: TestsRootEntry => new TestsRootView(x)
  }
}
