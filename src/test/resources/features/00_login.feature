Feature: Login with language management and footer

  Background:
    Given I launch the browser
    When I open the login page

# --- Home block UI test ---
  Scenario: Verify the home block with image and logo
    When I open the login page
    Then I should see the home block with image
    And I should see the brand logo

# --- Language and slogan test ---
  Scenario: Test language switch FR <-> EN with slogan
    When I switch to French
    Then I should see the title "Bienvenue à CG Direct !"
    And I should see the slogan "Votre réussite avant tout."
    And I should see the login text "Connectez-vous avec vos informations d'identification."

    When I switch to English
    Then I should see the title "Welcome to CG Direct!"
    And I should see the slogan "Driven by your success."
    And I should see the login text "Log in with your credentials."

# --- Main login ---
  Scenario: Display error messages if fields are empty
    When I click the login button without entering credentials
    Then the required field error messages should be displayed

  Scenario Outline: Login with invalid credentials
    When I get my credentials from Excel
    And I enter an email "<emailType>"
    And I enter a password "<passwordType>"
    And I submit my login
    Then the incorrect email or password message should be displayed

    Examples:
      | emailType | passwordType |
      | incorrect | correct |
      | correct | incorrect |
      | incorrect | incorrect |

  Scenario: Successful login with unique email
    When I get my credentials from Excel
    And I enter an email "correct"
    And I enter a password "correct"
    And I submit my login
    Then I should see the message

  Scenario Outline: Verify footer links
    Then I test the footer link "<Link>" and I should be redirected to "<Page>"

    Examples:
      | Link | Page |
      | Terms of Use | Terms |
      | Privacy Policy | Privacy |

