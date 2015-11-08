package protobufui.gui.controllers

import java.net.URL
import java.util.ResourceBundle
import javafx.beans.property.{SimpleObjectProperty, SimpleStringProperty}
import javafx.beans.value.ObservableValue
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.scene.control.{TableCell, TableColumn, TableView}
import javafx.util.Callback

import ipetoolkit.details.DetailsController
import protobufui.model.TestCaseEntry
import protobufui.service.testengine.ResultType._
import protobufui.service.testengine.runner.step.TestStepRunnerFactory
import protobufui.service.testengine.{ResultType, TestStepContext, TestStepResult}

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
 * Created by krever on 9/26/15.
 */


class TestCaseTabController extends Initializable with DetailsController {

  @FXML private var stepTable: TableView[TestStepResult] = _
  @FXML private var nameColumn: TableColumn[TestStepResult, String] = _
  @FXML private var statusColumn: TableColumn[TestStepResult, ResultType] = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    nameColumn.setCellValueFactory(new Callback[CellDataFeatures[TestStepResult, String], ObservableValue[String]] {
      override def call(param: CellDataFeatures[TestStepResult, String]): ObservableValue[String] = new SimpleStringProperty(param.getValue.step.getName)
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

  def runTests(stepTable: TableView[TestStepResult]): Unit = {
    import protobufui.gui.Main.actorSystem
    import protobufui.gui.Main.actorSystem.dispatcher
    val steps = stepTable.getItems.asScala.map(_.step)
    steps.zipWithIndex.foldLeft(Future((ResultType.Empty, new TestStepContext))) {
      case (args, (step, idx)) =>
        val newResult = for {
          (prevResult, ctx) <- args
          (curResullt, newCtx) <- TestStepRunnerFactory.createRunner(step).run(ctx)
        } yield (curResullt, newCtx)
        val curItem = stepTable.getItems.get(idx)
        newResult.foreach(x => stepTable.getItems.set(idx, curItem.copy(result = x._1)))
        newResult
    }
  }

}
