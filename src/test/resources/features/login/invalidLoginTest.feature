@RegressionTest
Feature: Login Functionality with InValid Data

  @OutlineDemo
  Scenario Outline:  InValid user Login
    Given User login into LoginPage
    When  User enters valid username "<UserName>" and password "<Password>"
    And   user clicks on Login Button
    Then User verifies Error Message should be displayed as "Invalid credentials"

    Examples:
      | UserName  | Password       |
      | Admin     | admin123123412 |
      | testAdmin | admin123       |
      | testAdmin | admin123123412 |
