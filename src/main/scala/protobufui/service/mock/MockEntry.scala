package protobufui.service.mock

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.mock.MockView

@XmlRootElement
class MockEntry extends WorkspaceEntry {
  override val view: WorkspaceEntryView = new MockView(this)
}
