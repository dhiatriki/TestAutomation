Feature: Agree after login

  Scenario: Continue from the page reached after login
    Given the browser is launched
    Then the welcome message should be displayed
    When the approval checkbox is clicked
    And the RDD start button is clicked
    And the accounts are selected
    And the open accounts button is clicked
    And the personal button is clicked
    And the address information is entered from Excel
    And the identity and tax information is entered from Excel
    And the Family member information is entered from Excel
