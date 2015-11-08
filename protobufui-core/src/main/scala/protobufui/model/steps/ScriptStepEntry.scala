package protobufui.model.steps

import javax.xml.bind.annotation.{XmlElement, XmlRootElement}

import ipetoolkit.workspace.WorkspaceEntry

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class ScriptStepEntry extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }

  @XmlElement
  var script = ""

}
