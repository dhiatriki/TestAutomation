Feature: SignUp with email verification



      # --- Home block UI test ---
  Scenario: Verify the home block with image and logo
    When open the sign up page
    Then the home sign up block with image should be visible
    And the brand logo sign up should be visible





  Scenario: Test language switch between French and English with slogan
    When the signup language is switched to French
    Then the signup title should be "Commençons !"
    And  the signup slogan should be "Votre réussite avant tout."
    And  the signup  text should be "Saisissez votre adresse électronique"

    When the signup language is switched to English
    Then the signup title should be "Let’s get started!"
    And  the signup slogan should be "Driven By your success."
    And  the signup  text should be "Enter your email address"

    # ---------- FOOTER LINKS ----------
  Scenario Outline: Verify footer links
    Given the browser is launched
    When open the sign up page

    Then the signup footer link "<link>" should redirect to "<page>"

    Examples:
      | link           | page     |
      | Terms of Use   | Terms    |
      | Privacy Policy | Privacy  |


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
    When enter the signup personal information
    And submit the personal information
    Then enter the phone verification code and submit
    Then verify the welcome message after phone verification