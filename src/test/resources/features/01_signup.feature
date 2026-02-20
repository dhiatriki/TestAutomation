Feature: Inscription avec vérification de l'email

  Scenario: Inscription avec un nouvel utilisateur et code de vérification
    Given je lance le navigateur
    When j'ouvre la page d'inscription
    And je récupère les informations d'inscription depuis Excel
    And je saisis l'email pour l'inscription
    And je saisis le mot de passe pour l'inscription
    And je confirme le mot de passe
    And je valide l'inscription
    Then je dois voir la page de vérification de l'email
    When je saisis le code de vérification reçu par email
    And je valide le code
    Then le compte doit être créé avec succès