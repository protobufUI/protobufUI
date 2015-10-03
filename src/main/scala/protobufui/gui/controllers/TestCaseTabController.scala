package protobufui.gui.controllers

import java.net.URL
import java.util.ResourceBundle
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.beans.value.ObservableValue
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.scene.control.{TableCell, TableColumn, TableView}
import javafx.util.Callback

import akka.actor.{Actor, ActorRef, Props}
import ipetoolkit.util.JavaFXDispatcher
import ipetoolkit.workspace.DetailsController
import protobufui.Main
import protobufui.test.ResultType._
import protobufui.test.step.TestStepResult
import protobufui.test.{ResultType, TestCaseEntry}

import scala.collection.JavaConverters._

/**
 * Created by krever on 9/26/15.
 */


class TestCaseTabController extends Initializable with DetailsController {

  @FXML private var stepTable: TableView[TestStepResult] = _
  @FXML private var nameColumn: TableColumn[TestStepResult, String] = _
  @FXML private var statusColumn: TableColumn[TestStepResult, ResultType] = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    nameColumn.setCellValueFactory(new Callback[CellDataFeatures[TestStepResult, String], ObservableValue[String]] {
      override def call(param: CellDataFeatures[TestStepResult, String]): ObservableValue[String] = new SimpleStringProperty(param.getValue.step.name)
    })
    statusColumn.setCellValueFactory(new Callback[CellDataFeatures[TestStepResult, ResultType], ObservableValue[ResultType]] {
      override def call(param: CellDataFeatures[TestStepResult, ResultType]): ObservableValue[ResultType] = new SimpleObjectProperty[ResultType](param.getValue.result)
    })
    statusColumn.setCellFactory(TestCaseTabController.statusCellFactory)
  }

  def testCase = model.asInstanceOf[TestCaseEntry]

  def refresh(): Unit = {
    val steps = testCase.getTestSteps.map(x => TestStepResult(x, ResultType.Empty))
    stepTable.getItems.setAll(steps.asJavaCollection)
  }

  def run(): Unit = {
    val steps = testCase.getTestSteps
    stepTable.getItems.setAll(steps.map(x => TestStepResult(x, ResultType.Empty)).asJavaCollection)
    TestCaseTabController.runTests(stepTable)
  }


}

object TestCaseTabController {

  val statusCellFactory =
    new Callback[TableColumn[TestStepResult, ResultType], TableCell[TestStepResult, ResultType]]() {
      override def call(param: TableColumn[TestStepResult, ResultType]): TableCell[TestStepResult, ResultType] = {
        val cell = new TableCell[TestStepResult, ResultType]() {
          override def updateItem(item: ResultType, empty: Boolean): Unit = {

            def getString: String = if (empty) null else if (getItem == null) "" else getItem.toString
            def getColor = Option(getItem).collect {
              case ResultType.Success => "green"
              case ResultType.Failure => "red"
              case ResultType.Empty => "white"
            }.getOrElse("white")

            super.updateItem(item, empty)
            setText(getString)
            setStyle("-fx-background-color:" + getColor)
          }
        }
        cell
      }
    }

  def runTests(stepTable: TableView[TestStepResult]): ActorRef = Main.actorSystem.actorOf(Props(new TestRunController(stepTable)).withDispatcher(JavaFXDispatcher.Id))

  private class TestRunController(stepTable: TableView[TestStepResult]) extends Actor {

    @throws[Exception](classOf[Exception])
    override def preStart(): Unit = {
      /*val runner = context.system.actorOf(Props[TestStepsRunner])
      runner ! Run(stepTable.getItems.asScala.map(_.step).toList)*/
    }

    override def receive: Receive = ??? /*{ TODO actor wyciety, przerobic na future
      case SingleStepResult(idx, result) =>
        val curItem = stepTable.getItems.get(idx)
        stepTable.getItems.set(idx, curItem.copy(result = result))
    }*/
  }

}
