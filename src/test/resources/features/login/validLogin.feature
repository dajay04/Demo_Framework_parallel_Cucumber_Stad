@LoginTest
Feature: Login Functionality with Valid Data

  Background:
    Given User login into LoginPage
    When  User enters valid username "Admin" and password "admin123"

  @DemoPOM
  Scenario:  Valid user Login 1
    And   user clicks on Login Button
    Then  User redirected to Dashboard Page
