package protobufui.util.dialog

import java.io.File
import javafx.application.Platform
import javafx.beans.property.StringProperty
import javafx.scene.control.TextInputDialog

import protobufui.Globals

/**
 * Created by humblehound on 03.10.15.
 */

object Dialog {
  def rename(stringProperty: StringProperty) : Unit = {
    val newNameDialog = new TextInputDialog()
    newNameDialog.setTitle("Rename " + stringProperty.get)
    newNameDialog.getEditor.setText(stringProperty.get)
    newNameDialog.setHeaderText(null)
    newNameDialog.setGraphic(null)
    val newName = newNameDialog.showAndWait()
    if(newName.isPresent){
      stringProperty.setValue(newName.get)
    }
  }

  def setWorkspaceRoot() = {
    val dialog = new TextInputDialog(System.getProperty("user.home")+File.separator+".protobufUI")
    dialog.setTitle("Workspace Root Selector")
    dialog.setContentText("Insert path to the workspace root")
    dialog.setHeaderText(null)
    val result = dialog.showAndWait()
    if(result.isPresent){
      Globals.setProperty(Globals.Keys.workspaceRoot, result.get())
    }else{
      Platform.exit()
    }
  }

}
