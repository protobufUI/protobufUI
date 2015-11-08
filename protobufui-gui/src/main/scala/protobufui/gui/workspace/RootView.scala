package protobufui.gui.workspace

import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.model.{TestsRootEntry, MocksRootEntry, MessagesRootEntry}


class RootView(val model: WorkspaceEntry) extends WorkspaceEntryView {
  override def childrenToViews: PartialFunction[WorkspaceEntry, WorkspaceEntryView] = {
    case x: MessagesRootEntry => new MessagesRootView(x)
    case x: MocksRootEntry => new MocksRootView(x)
    case x: TestsRootEntry => new TestsRootView(x)
  }
}
