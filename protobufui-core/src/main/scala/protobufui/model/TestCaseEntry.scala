package protobufui.model

import javax.xml.bind.annotation.XmlRootElement

import akka.actor.ActorSystem
import ipetoolkit.workspace.WorkspaceEntry

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

@XmlRootElement
class TestCaseEntry extends WorkspaceEntry {

  def this(name:String){
    this()
    setName(name)
  }

  def getTestSteps: List[WorkspaceEntry] = children


}

