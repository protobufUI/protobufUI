package protobufui.model.steps

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.WorkspaceEntry

import scala.concurrent.Future

/**
 * Created by humblehound on 26.09.15.
 */
@XmlRootElement
class SetSpecsStepEntry extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }

  var propertyValueMap : Map[String, String] = Map()

}
