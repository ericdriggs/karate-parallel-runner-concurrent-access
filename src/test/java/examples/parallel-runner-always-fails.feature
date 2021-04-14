Feature:

  Scenario: foo test 1

    * print 'foo test 1'
    * def expectedHello = "hello foo"
    * def actualHello = Hello.getHello("foo")
    * match expectedHello == actualHello

  Scenario: foo test 2

    * print 'foo test 2'
    * def expectedHello = "hello foo"
    * def actualHello = Hello.getHello("foo")
    * match expectedHello == actualHello
