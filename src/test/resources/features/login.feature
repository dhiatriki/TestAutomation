Feature: Test de connexion

  Scenario: Connexion réussie
    Given je lance le navigateur
    When j'ouvre la page de login
    And je saisis username "tomsmith"
    And je saisis password "SuperSecretPassword!"
    And je clique sur login
    Then je dois voir le message "You logged into a secure area!"
