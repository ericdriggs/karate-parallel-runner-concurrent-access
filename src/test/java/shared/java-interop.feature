@ignore
Feature: Setup java interop

  Scenario: Setup java interop

    * def AssertDate = Java.type('shared.AssertDate')
    * def AssertJson = Java.type('shared.AssertJson')