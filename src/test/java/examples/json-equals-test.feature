Feature:

  Scenario: Test Arrays

    * def expectedJson =
    """
    [
      {
        "description": "description1",
        "created_datetime": "2019-05-29T08:04:57.168441-07:00"
      }
    ]
    """

    * def actualJson =
    """
    [
      {
        "description": "description1",
        "created_datetime": "2019-05-29T08:04:57.168441-07:00",
        "extraProperty": "property1"
      }
    ]
    """

    * call assertJsonEquals { "expected": #(expectedJson), "actual": #(actualJson) }

  Scenario: Test Strict Success

    * def req = {};
  * set req.expected = { "a" : 1, "b": 2, "c": 3, "d": 4 };
  * set req.actual = { "a" : 1, "b": 2,  "d": 4, "c": 3};
  * call assertJsonEqualsStrict (req)

  Scenario: Test Strict Failure, extra property

    * def runTest =
    """
    function() {
      var req = { }
      req.actual = { "a" : 1, "b": 2, "c": 3, "d": 4 };
      req.expected = { "a" : 1, "b": 2, "c": 3};
      karate.log("### req: " + JSON.stringify(req));

      try {
        assertJsonEqualsStrict (req)
      }
      catch (ex) {
        karate.log("###  exception caught ");
        return;
      }
      var AssertionError = Java.type('java.lang.AssertionError');
      throw new AssertionError("expected to throw")
    }
    """
    * call runTest

  Scenario: When expected property missing, fail

    * def runTest =
    """
    function() {
      var req = { }
      req.expected = { "a" : 1, "b": 2};
      req.actual = { "a" : 1};

      karate.log("### req: " + JSON.stringify(req));

      try {
        assertJsonEqualsStrict (req)
      }
      catch (ex) {
        karate.log("###  exception caught ");
        return;
      }
      var AssertionError = Java.type('java.lang.AssertionError');
      throw new AssertionError("expected to throw")
    }
    """
    * call runTest


  Scenario: Test Json objects

    * call assertJsonEquals { expected: { a : 1 }, actual: { a : 1 } }
    * call assertJsonEquals { expected: { a : 1}, actual: { a : 1, b: 1  } }



