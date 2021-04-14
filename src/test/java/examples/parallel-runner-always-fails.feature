#Given using config = karate.callSingle to define a Java.type variable
#When execute parallel runner,
#Then get reliable "Multi threaded access requested by thread" error

#set to @ignore if you want parallel-sometimes-fails.feature to pass sometimes
#@ignore
Feature:

  Scenario: foo test 1

    * karate.log('### foo test 1')
    * def expectedHello = "hello foo"
    * def actualHello = Hello.getHello("foo")
    * match expectedHello == actualHello

  Scenario: foo test 2

    * karate.log('### foo test 2')
    * def expectedHello = "hello foo"
    * def actualHello = Hello.getHello("foo")
    * match expectedHello == actualHello
