Feature: SignUp with email verification

  Scenario: SignUp with a new user and verification code
    Given the browser is launched
    When open the sign up page
    And get the sign up information from Excel
    And enter the email for sign up
    And enter the password for sign up
    And confirm the password
    And submit the sign up form
    Then see the email verification page
    When enter the verification code received by email
    And submit the code
    Then account should be created successfully