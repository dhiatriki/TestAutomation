Feature: Connexion avec email dynamique depuis Excel

  Scenario: Connexion réussie avec email unique
    Given je lance le navigateur
    When j'ouvre la page de login
    And je récupère mes identifiants depuis Excel
    And je saisis mes identifiants dynamiques
    And je valide mon login
    Then je dois voir le message "Welcome on board Sujet10!"