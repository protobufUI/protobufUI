package protobufui.model

import javax.xml.bind.annotation.XmlRootElement

import akka.actor.ActorSystem
import ipetoolkit.workspace.WorkspaceEntry

import scala.concurrent.Future

@XmlRootElement
class TestSuite extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }

  def getTestCases: List[TestCaseEntry] = children.collect { case t: TestCaseEntry => t }
}
