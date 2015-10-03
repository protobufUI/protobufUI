package protobufui.test.format

import protobufui.test.TestSuiteResult
import protobufui.test.format.generated.{Testcase, Testsuite, Testsuites}

/**
 * Created by krever on 10/3/15.
 */
object TestReportCreator {

  //TODO nie skonczone
  def createReport(testSuitesResults: List[TestSuiteResult]): Testsuites = {
    val aTestSuites = new Testsuites
    testSuitesResults.foreach { suiteResult =>
      val aTestSuite = new Testsuite()
      suiteResult.casesResults.foreach { caseResult =>
        val aTestCase = new Testcase()
        aTestCase.setStatus(caseResult.status.toString)
        aTestSuite.getTestcase.add(aTestCase)
      }
      aTestSuites.getTestsuite.add(aTestSuite)
    }
    aTestSuites
  }

}
