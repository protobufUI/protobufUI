package protobufui.gui.controllers

import java.net.URL
import java.util.ResourceBundle
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Node
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.scene.control._
import javafx.scene.control.cell.{PropertyValueFactory, TextFieldTableCell}

import ipetoolkit.details.DetailsController
import ipetoolkit.workspace.WorkspaceEntry
import protobufui.gui.util.Editable
import protobufui.model.steps.SetSpecsStepEntry

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
 * Created by humblehound on 26.09.15.
 */
class SetSpecsController extends Initializable with DetailsController with Editable {

  @FXML var specsTable : TableView[PropertyKeyValue] = _
  @FXML var testName: TextField = _
  @FXML var addTableRow: Button = _

  var setSpecsStep : SetSpecsStepEntry = _

  override protected def pageElements: List[Node] = List(testName, addTableRow)

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    initEditionControls()
    val firstColumn = new TableColumn[PropertyKeyValue, String]("Property")
    firstColumn.setCellValueFactory(new PropertyValueFactory[PropertyKeyValue, String]("key"))
    firstColumn.setCellFactory(TextFieldTableCell.forTableColumn[PropertyKeyValue]())
    firstColumn.setEditable(true)
    firstColumn.setOnEditCommit(new EventHandler[CellEditEvent[PropertyKeyValue, String]] {
      override def handle(event: CellEditEvent[PropertyKeyValue, String]): Unit =
        event.getTableView.getItems.get(event.getTablePosition.getRow).setKey(event.getNewValue)
    })

    val secondColumn = new TableColumn[PropertyKeyValue, String]("Value")
    secondColumn.setCellFactory(TextFieldTableCell.forTableColumn[PropertyKeyValue]())
    secondColumn.setCellValueFactory(new PropertyValueFactory[PropertyKeyValue, String]("value"))
    secondColumn.setEditable(true)
    secondColumn.setOnEditCommit(new EventHandler[CellEditEvent[PropertyKeyValue, String]] {
      override def handle(event: CellEditEvent[PropertyKeyValue, String]): Unit =
        event.getTableView.getItems.get(event.getTablePosition.getRow).setValue(event.getNewValue)
    })
    specsTable.getColumns.addAll(firstColumn, secondColumn)
  }

  override def setModel(entry: WorkspaceEntry) = {
    this.setSpecsStep = entry.asInstanceOf[SetSpecsStepEntry]
    loadFormValuesFromTest()
  }

  override protected def onEditCompleted(): Unit = saveFormValuesToTest()

  override protected def onEditCancelled(): Unit = loadFormValuesFromTest()

  override protected def validate(): Boolean = true

  override protected def onEditStart(): Unit = {}

  private def saveFormValuesToTest() = {
    import scala.collection.JavaConverters._
    setSpecsStep.propertyValueMap = specsTable.getItems.asScala.map(x => x.getKey -> x.getValue).toMap
  }

  private def loadFormValuesFromTest(): Unit ={
    specsTable.setItems(
      FXCollections.observableArrayList(
      setSpecsStep.propertyValueMap.toList.map{
        case (key, value) => new PropertyKeyValue(key, value)}
        .asJava))
  }

  @FXML private def addNewRow(): Unit ={
    specsTable.getItems.add(new PropertyKeyValue("", ""))
  }
}

case class PropertyKeyValue(@BeanProperty var key: String, @BeanProperty var value: String)
