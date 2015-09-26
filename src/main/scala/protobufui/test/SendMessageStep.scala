package protobufui.test

import com.google.protobuf.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView
import protobufui.service.source.ClassesContainer.MessageClass

/**
 * Created by humblehound on 26.09.15.
 */
class SendMessageStep() extends WorkspaceEntry{
  var testName : String = "Send message"
  var ipAddress : String = "192.168.0.1"
  var ipPort : String = "80"
  var messageClass: MessageClass = _
  var message: Message = _

  override val view: WorkspaceEntryView = new TestStepView(this, TestStepType.SendMessage)

}


