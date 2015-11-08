package protobufui.service.testengine

import ipetoolkit.workspace.WorkspaceEntry
import protobufui.service.testengine.ResultType.ResultType

/**
 * Created by krever on 11/7/15.
 */
case class TestStepResult(step: WorkspaceEntry, result: ResultType)
