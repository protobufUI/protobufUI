package protobufui.gui.workspace.base

import javafx.beans.property.{SimpleStringProperty, StringProperty}
import javafx.scene.control.ContextMenu
import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.util.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.mock.MocksRootEntry
import protobufui.test.TestSuite
import protobufui.test.format.generated.Testsuite

@XmlRootElement
class RootEntry extends WorkspaceEntry {

  override val view: WorkspaceEntryView = new RootView(this)

  def getTests: List[TestSuite] = children.collectFirst{case x: MessagesRootEntry => x}.get.children.collect{case x: TestSuite => x}

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

class RootView(val model: WorkspaceEntry) extends WorkspaceEntryView {

  override val nameProperty: StringProperty = new SimpleStringProperty("Root")

  override def contextMenu: Option[ContextMenu] = None

  override def detailsOpener: Option[Message] = None
}
