package protobufui.service.mock

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.WorkspaceEntry

@XmlRootElement
class MockEntry extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }
}
