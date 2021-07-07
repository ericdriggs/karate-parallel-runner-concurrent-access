@ignore
Feature: common setup functionality for tests

  Scenario:

    * def Hello = Java.type('examples.Hello')

    * def sayHello =
    """
    function (message) {
      var HelloFactory = Java.type('examples.Hello').sayHelloFactory();
      return Hello.sayHello(message)
    }
    """


