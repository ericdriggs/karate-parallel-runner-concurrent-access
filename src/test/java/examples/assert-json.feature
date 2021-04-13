@ignore
Feature:
  Scenario:

    * def assertJsonEquals =
    """
    function (req) {
      var AssertJson = Java.type('examples.AssertJson');
      var expectedString = JSON.stringify(req.expected)
      var actualString = JSON.stringify(req.actual)
      karate.log("expected: " + expectedString + ", actual: " + actualString);
      AssertJson.jsonUnitEquals(expectedString, actualString);
    }
    """

    * def assertJsonEqualsStrict =
    """
    function (req) {
      karate.log("req: " + JSON.stringify(req));
      var AssertJson = Java.type('examples.AssertJson');
      var expectedString = JSON.stringify(req.expected)
      var actualString = JSON.stringify(req.actual)
      karate.log("expected: " + expectedString + ", actual: " + actualString);
      AssertJson.jsonUnitEqualsStrict(expectedString, actualString);
    }
    """