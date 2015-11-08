package protobufui.model

import javax.xml.bind.annotation.XmlRootElement
import ipetoolkit.workspace.WorkspaceEntry



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
