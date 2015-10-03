package protobufui.test

import protobufui.test.step.TestStepResult


case class TestCaseResult(testCase: TestCaseEntry, stepsResults: List[TestStepResult]) {
  val status: ResultType.ResultType = if (stepsResults.forall(_.result == ResultType.Success)) ResultType.Success else ResultType.Failure
}
