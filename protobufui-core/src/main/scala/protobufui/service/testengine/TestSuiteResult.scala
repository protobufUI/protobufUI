package protobufui.service.testengine

import protobufui.model.TestSuite

case class TestSuiteResult(suite: TestSuite, casesResults: List[TestCaseResult])