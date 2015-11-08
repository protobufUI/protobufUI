package protobufui.model

import javax.xml.bind.annotation.XmlRootElement

import ipetoolkit.workspace.WorkspaceEntry


@XmlRootElement
class TestsRootEntry extends WorkspaceEntry {
  setName("Tests")
}