Feature: Login

  Scenario: Login with the account created during signup
    Given the browser is launched
    When the login page is opened
    And credentials are retrieved from Excel
    And an email "correct" is entered
    And a password "correct" is entered
    And the login form is submitted
    Then the welcome message should be displayed
