Feature: Login with language management and footer verification

  Background:
    Given the browser is launched
    When the login page is opened

  # --- Home block UI test ---
  Scenario: Verify the home block with image and logo
    When the login page is opened
    Then the home block with image should be visible
    And the brand logo should be visible

  # --- Language and slogan test ---
  Scenario: Test language switch between French and English with slogan
    When the language is switched to French
    Then the title should be "Bienvenue à CG Direct !"
    And the slogan should be "Votre réussite avant tout."
    And the login text should be "Connectez-vous avec vos informations d'identification."

    When the language is switched to English
    Then the title should be "Welcome to CG Direct!"
    And the slogan should be "Driven by your success."
    And the login text should be "Log in with your credentials."

  Scenario: Display error messages when fields are empty
    When the login button is clicked without entering credentials
    Then the required field error messages should be displayed

  Scenario Outline: Login with invalid credentials
    When credentials are retrieved from Excel
    And an email "<emailType>" is entered
    And a password "<passwordType>" is entered
    And the login form is submitted
    Then the incorrect email or password message should be displayed

    Examples:
      | emailType  | passwordType |
      | incorrect  | correct      |
      | correct    | incorrect    |
      | incorrect  | incorrect    |

  Scenario: Successful login with correct credentials
    When credentials are retrieved from Excel
    And an email "correct" is entered
    And a password "correct" is entered
    And the login form is submitted
    Then the welcome message should be displayed

  Scenario Outline: Verify footer links redirection
    Then the footer link "<Link>" should redirect to "<Page>"

    Examples:
      | Link           | Page    |
      | Terms of Use   | Terms   |
      | Privacy Policy | Privacy |