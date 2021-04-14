@ignore
Feature: common setup functionality for tests

  Scenario:

    * def Hello = Java.type('examples.Hello')

    * def getHello =
    """
    function (message) {
      var Hello = Java.type('examples.Hello');
      return Hello.getHello(message)
    }
    """


