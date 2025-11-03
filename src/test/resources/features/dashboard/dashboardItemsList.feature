@RegressionTest
Feature: Login Functionality with Valid Data

  Background:
    Given User login into LoginPage
    When  User enters valid username "Admin" and password "admin123"
    And   user clicks on Login Button
    Then  User redirected to Dashboard Page

  Scenario: Verifying Element on Dashboard
    Then User verifies elements on Dashboard menu items:
      | Admin |
      | PIM   |
      | Leave |
