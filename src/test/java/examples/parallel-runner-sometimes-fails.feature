  # Given paralel-runner-always-fails.feature has annotation @ignore
  # When run ParallelRunnerTest
  # Then this test sometimes passes and sometimes fails
  Feature:

    Scenario: bar test

      * karate.log('### bar test')
      * def expectedHello = "hello bar"
      * def actualHello = getHello("bar")
      * match expectedHello == actualHello

    Scenario: baz test

      * karate.log('### baz test')
      * def expectedHello = "hello baz"
      * def actualHello = getHello("baz")
      * match expectedHello == actualHello
