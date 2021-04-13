Feature:

  Scenario: match a
    * def req =
    """
    {
      actual: {a: 1,  b: 2, c: 3},
      expected: {a: 1,  c: 3, b: 2 }
    }
    """
    * assertJsonEqualsStrict(req)

  Scenario: match b
    * def req =
    """
    {
      actual: {a: 1,  b: 2, c: 3},
      expected: {a: 1,  c: 3, b: 2 }
    }
    """
    * assertJsonEqualsStrict(req)

  Scenario: match c
    * def req =
    """
    {
      actual: {a: 1,  b: 2, c: 3},
      expected: {a: 1,  c: 3, b: 2 }
    }
    """
    * assertJsonEqualsStrict(req)
