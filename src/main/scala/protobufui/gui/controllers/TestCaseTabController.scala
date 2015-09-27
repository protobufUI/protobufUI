package protobufui.gui.controllers

import ipetoolkit.workspace.DetailsController
import protobufui.test.TestCaseEntry

/**
 * Created by krever on 9/26/15.
 */
class TestCaseTabController extends DetailsController {

  def testCase = model.asInstanceOf[TestCaseEntry]


}
