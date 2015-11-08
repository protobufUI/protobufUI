package protobufui.service.testengine.report

import protobufui.service.testengine.{ResultType, TestSuiteResult}
import protobufui.service.testengine.report.generated.{Failure, Testcase, Testsuite, Testsuites}
import scala.collection.JavaConverters._

/**
 * Created by krever on 10/3/15.
 */
object TestReportCreator {

  //TODO nie skonczone
  def createReport(testSuitesResults: List[TestSuiteResult]): Testsuites = {
    val aTestSuites = new Testsuites
    testSuitesResults.foreach { suiteResult =>
      val aTestSuite = new Testsuite()
      aTestSuite.setName(suiteResult.suite.getName)
      suiteResult.casesResults.foreach { caseResult =>
        val aTestCase = new Testcase()
        aTestCase.setStatus(caseResult.status.toString)
        aTestCase.setName(caseResult.testCase.getName)
        val failures = caseResult
          .stepsResults
          .filter(_.result == ResultType.Failure)
          .map{r => val f = new Failure();f.setContent(r.step.getName); f}
        aTestCase.getFailure.addAll(failures.asJavaCollection)
        aTestSuite.getTestcase.add(aTestCase)
      }
      aTestSuites.getTestsuite.add(aTestSuite)
    }
    aTestSuites
  }

}
