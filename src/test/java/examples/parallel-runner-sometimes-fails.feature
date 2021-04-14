Feature:

  Scenario: bar test

    * print 'bar test'
    * def expectedHello = "hello bar"
    * def actualHello = Hello.getHello("bar")
    * match expectedHello == actualHello

  Scenario: baz test

    * print 'baz test'
    * def expectedHello = "hello baz"
    * def actualHello = Hello.getHello("baz")
    * match expectedHello == actualHello
