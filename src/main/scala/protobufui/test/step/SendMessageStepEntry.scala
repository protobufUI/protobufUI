package protobufui.test.step

import javax.xml.bind.annotation.{XmlElement, XmlRootElement}

import com.google.protobuf.Message
import ipetoolkit.workspace.{WorkspaceEntry, WorkspaceEntryView}
import protobufui.gui.workspace.test.TestStepView
import protobufui.service.source.ClassesContainer.MessageClass

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SendMessageStepEntry extends WorkspaceEntry with TestStep {

  @XmlElement
  var ipAddress : String = "192.168.0.1"
  @XmlElement
  var ipPort : String = "80"
  @XmlElement
  var messageClass: MessageClass = _

  var message: Message = _

  override val view: WorkspaceEntryView = new TestStepView(this, "/fxml/stepMessage.fxml", "SendMessageStep")

  override def run(context: TestStepContext): (TestStepResult, TestStepContext) = (new TestStepResult(ResultType.Success), context)

  override def name: String = nameProperty.get()
}


