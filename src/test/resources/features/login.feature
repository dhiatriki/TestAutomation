Feature: Connexion avec gestion de langue et footer

  Background:
    Given je lance le navigateur
    When j'ouvre la page de login


# --- Test UI bloc accueil ---
  Scenario: Vérification du bloc d'accueil avec image et logo
    When j'ouvre la page de login
    Then je dois voir le bloc d'accueil avec image
    And je dois voir le logo de la marque


# --- Test langue et slogan ---
  Scenario: Test du changement de langue FR EN avec slogan

    When je bascule en français
    Then je dois voir le titre "Bienvenue à CG Direct !"
    And je dois voir le slogan "Votre réussite avant tout."
    And je dois voir le texte de connexion "Connectez-vous avec vos informations d'identification."

    When je bascule en anglais
    Then je dois voir le titre "Welcome to CG Direct!"
    And je dois voir le slogan "Driven by your success."
    And je dois voir le texte de connexion "Log in with your credentials."

# --- Login principal ---
  Scenario: Affichage des messages d'erreur si les champs sont vides
    When je clique sur le bouton login sans saisir les identifiants
    Then les messages d'erreur des champs obligatoires doivent être affichés


  Scenario Outline: Connexion avec identifiants invalides
    When je récupère mes identifiants depuis Excel
    And je saisis un email "<emailType>"
    And je saisis un password "<passwordType>"
    And je valide mon login
    Then le message email ou mot de passe incorrect doit être affiché

    Examples:
      | emailType | passwordType |
      | incorrect | correct |
      | correct | incorrect |
      | incorrect | incorrect |


  Scenario: Connexion réussie avec email unique
    When je récupère mes identifiants depuis Excel
    And je saisis un email "correct"
    And je saisis un password "correct"
    And je valide mon login
    Then je dois voir le message


  Scenario Outline: Vérification des liens du footer
    Then je teste le lien footer "<Lien>" et je dois être redirigé vers "<Page>"

    Examples:
      | Lien | Page |
      | Terms of Use | Terms |
      | Privacy Policy | Privacy |