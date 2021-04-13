@ignore
Feature:

  ######################################################################################################################
  ## compares expected to actual, ignoring extra fields in actual
  ## supported regex for expected. e.g. : "${json-unit.regex}2018-01-01.*"

  ### assertJsonEquals examples: ###
  #    #usage
  #    * call assertJsonEquals { expected: #(expected), actual: #(actual) }

  #    #pass scenarios:
  #    * call assertJsonEquals { expected: { a : 1 }, actual: { a : 1 } }
  #    * call assertJsonEquals { expected: { a : 1}, actual: { a : 1, b: 1  } }

  #    #fail scenarios:
  #    * call assertJsonEquals { expected: { a : 1 }, actual: { b : 1 } }
  #    * call assertJsonEquals { expected: { a : 1, b: 1}, actual: { a : 1 } }

  Background:


  Scenario:

    * def assertJsonEquals =
    """
    function (req) {
      var AssertJson = Java.type('shared.AssertJson');
      var expectedString = JSON.stringify(req.expected)
      var actualString = JSON.stringify(req.actual)
      karate.log("expected: " + expectedString + ", actual: " + actualString);
      AssertJson.jsonUnitEquals(expectedString, actualString);
    }
    """

    * def assertJsonEqualsStrict =
    """
    function (req) {
      var AssertJson = Java.type('shared.AssertJson');
      var expectedString = JSON.stringify(req.expected)
      var actualString = JSON.stringify(req.actual)
      karate.log("expected: " + expectedString + ", actual: " + actualString);
      AssertJson.jsonUnitEqualsStrict(expectedString, actualString);
    }
    """

    ####################################################################################################################

    ## same as assertJsonEquals, only strips time from iso8061 date strings in both expected and actual
    * def assertJsonEqualsStripTimes =
    """
    function (req) {
      karate.log('req: ' + req);
      var AssertJson = Java.type('shared.AssertJson');
      var expectedString = JSON.stringify(req.expected);
      var actualString = JSON.stringify(req.actual);
      karate.log("expected: " + expectedString + ", actual: " + actualString);
      AssertJson.jsonUnitEqualsStripTimes(expectedString, actualString);
    }
    """

    ####################################################################################################################

    # usage
    #  * call assertJsonEqualsWithDateZone { expected: { date : "date-offset-y:0,m:0,d:0" }, actual: { date: #(AssertDate.now()) } }
    * def assertJsonEqualsWithDateOffset =
    """
    function (req) {
      karate.log('req: ' + req);
      var AssertJson = Java.type('shared.AssertJson');
      var expectedString = JSON.stringify(req.expected)
      var actualString = JSON.stringify(req.actual)
      karate.log("expected: " + expectedString + ", actual: " + actualString);

      AssertJson.assertJsonEqualsWithDateOffset(expectedString, actualString);
    }
    """
    * def assertJsonEqualsWithDateZone = assertJsonEqualsWithDateOffset