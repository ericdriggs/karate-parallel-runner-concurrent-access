@ignore
Feature: common setup functionality for tests

  Scenario:
    * call read('classpath:shared/java-interop.feature')
    * call read('classpath:shared/assert-json-equals.feature')