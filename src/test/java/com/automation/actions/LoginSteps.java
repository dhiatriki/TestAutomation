package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import io.cucumber.java.en.*;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/LoginData.xlsx");
    private String email;
    private String password;

    @Given("je lance le navigateur")
    public void lancer_nav() {
        // Le navigateur est déjà initialisé dans BaseClass
        System.out.println("Browser already launched by BaseClass.");
    }

    @When("j'ouvre la page de login")
    public void open_login_page() {
        String url = BaseClass.prop.getProperty("url");
        BaseClass.driver.get(url);
    }

    @When("je récupère mes identifiants depuis Excel")
    public void read_credentials_from_excel() {
        email = excelUtils.getEmail();      // Email dynamique avec compteur
        password = excelUtils.getPassword(); // Mot de passe sécurisé
        System.out.println("Test avec email : " + email);
    }

    @When("je saisis mes identifiants dynamiques")
    public void enter_dynamic_credentials() {
        locator.getLocator("_id_username").sendKeys(email);
        locator.getLocator("_id_password").sendKeys(password);
    }

    @When("je valide mon login")
    public void click_login() {
        locator.getLocator("_cssSelector_login_button").click();
    }

    @Then("je dois voir le message {string}")
    public void verify_message(String msg) {
        String text = locator.getLocator("_cssSelector_welcome_message").getText();
        System.out.println("Message affiché : " + text);
        assertTrue("Login échoué !", text.contains(msg));

        // Incrémenter le compteur Excel après chaque test
        excelUtils.incrementCount();
        excelUtils.closeWorkbook();
    }
}