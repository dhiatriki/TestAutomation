Feature: Test de connexion

  Scenario: Connexion réussie
    Given je lance le navigateur
    When j'ouvre la page de login
    And je saisis username "test.username"
    And je saisis password "test.password"
    And je valide mon login
    Then je dois voir le message "You logged into a secure area!"
