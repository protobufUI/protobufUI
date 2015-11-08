package protobufui.service.testengine

import protobufui.model.TestCaseEntry


case class TestCaseResult(testCase: TestCaseEntry, stepsResults: List[TestStepResult]) {
  val status: ResultType.ResultType = if (stepsResults.forall(_.result == ResultType.Success)) ResultType.Success else ResultType.Failure
}
