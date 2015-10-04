package protobufui.gui

import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button

/**
 * Created by humblehound on 03.10.15.
 */
trait Editable {

  @FXML var editBtn: Button = _
  @FXML var cancelBtn: Button = _
  @FXML var saveBtn: Button = _

  protected def initEditionControls(): Unit ={
    cancelBtn.setDisable(true)
    editBtn.setDisable(true)
  }

  protected def onEditCompleted()
  protected def onEditCancelled()
  protected def onEditStart()
  protected def validate() : Boolean
  protected def pageElements : List[Node]

  private def setEditEnabled(isEnabled: Boolean): Unit ={
    pageElements.foreach(node => node.setDisable(!isEnabled))
    cancelBtn.setDisable(!isEnabled)
    saveBtn.setDisable(!isEnabled)
    editBtn.setDisable(isEnabled)
  }


  @FXML
  protected def edit(): Unit ={
    setEditEnabled(true)
    onEditStart()
  }

  @FXML
  protected def cancel(): Unit ={
    setEditEnabled(false)
    onEditCancelled()
  }

  @FXML
  protected def save(): Unit ={
    if(validate()){
      setEditEnabled(false)
      onEditCompleted()
    }else{
      // Show what's wrong
    }
  }
}
