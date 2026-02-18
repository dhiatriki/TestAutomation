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
    private String mainWindow;

    // ---------------- NAVIGATION ----------------

    @Given("je lance le navigateur")
    public void lancer_nav() {
        mainWindow = BaseClass.driver.getWindowHandle();
        System.out.println("Browser ready or already launched.");
    }

    @When("j'ouvre la page de login")
    public void open_login_page() {
        BaseClass.driver.get(BaseClass.prop.getProperty("url"));
    }

    // ---------------- LANGUE ----------------

    @When("je bascule en français")
    public void switch_to_french() {
        locator.getLocator("_id_switchToFrench").click();
    }

    @When("je bascule en anglais")
    public void switch_to_english() {
        locator.getLocator("_id_switchToEnglish").click();
    }

    // ---------------- TITRE ----------------

    @Then("je dois voir le titre {string}")
    public void verify_title(String expected) {

        String actual = locator.getLocator("_cssSelector_title")
                .getText()
                .trim();

        assertTrue(
                "Titre attendu : " + expected + " mais trouvé : " + actual,
                actual.equals(expected)
        );
    }

    // ---------------- SLOGAN ----------------

    @Then("je dois voir le slogan {string}")
    public void verify_slogan(String expectedSlogan) {
        String actual = locator.getLocator("_className_brand-slogan").getText().trim();
        assertTrue(actual.equals(expectedSlogan));
    }

    // ---------------- TEXTE LOGIN ----------------
    @Then("je dois voir le texte de connexion {string}")
    public void verify_login_text(String expected) {

        String actual = locator.getLocator("_cssSelector_login_text")
                .getText()
                .trim();

        assertTrue(
                "Texte attendu : " + expected + " mais trouvé : " + actual,
                actual.equals(expected)
        );
    }

    // ---------------- LOGIN ----------------

    @When("je récupère mes identifiants depuis Excel")
    public void read_credentials() {
        email = excelUtils.getEmail();
        password = excelUtils.getPassword();
    }

    @When("je saisis un email {string}")
    public void enter_email(String type) {
        locator.getLocator("_id_username").clear();
        locator.getLocator("_id_username")
                .sendKeys(type.equals("incorrect") ? email + "1" : email);
    }

    @When("je saisis un password {string}")
    public void enter_password(String type) {
        locator.getLocator("_id_password").clear();
        locator.getLocator("_id_password")
                .sendKeys(type.equals("incorrect") ? password + "1" : password);
    }

    @When("je valide mon login")
    public void click_login() {
        locator.getLocator("_cssSelector_login_button").click();
    }

    @Then("je dois voir le message")
    public void verify_success() {
        String text = locator.getLocator("_cssSelector_welcome_message").getText();
        assertTrue(text.contains("Welcome") || text.contains("Bienvenue"));
        excelUtils.closeWorkbook();
    }

    @When("je clique sur le bouton login sans saisir les identifiants")
    public void click_login_empty() {
        locator.getLocator("_cssSelector_login_button").click();
    }

    @Then("les messages d'erreur des champs obligatoires doivent être affichés")
    public void verify_required_errors() {

        String userErr = locator.getLocator("_id_Username-error").getText();
        String passErr = locator.getLocator("_id_Password-error").getText();

        assertTrue(userErr.length() > 0);
        assertTrue(passErr.length() > 0);
    }

    @Then("le message email ou mot de passe incorrect doit être affiché")
    public void verify_invalid_login() {
        String msg = locator.getLocator("_xpath_error_message").getText();
        assertTrue(msg.length() > 0);
    }

    // ---------------- FOOTER ----------------

    @Then("je teste le lien footer {string} et je dois être redirigé vers {string}")
    public void test_footer(String lien, String page) {

        switch (lien) {
            case "Terms of Use":
                locator.getLocator("_cssSelector_terms_of_use").click();
                break;
            case "Privacy Policy":
                locator.getLocator("_xpath_privacy").click();
                break;
        }

        for (String w : BaseClass.driver.getWindowHandles()) {
            if (!w.equals(mainWindow))
                BaseClass.driver.switchTo().window(w);
        }

        String url = BaseClass.driver.getCurrentUrl().toLowerCase();

        if (page.equals("Terms"))
            assertTrue(url.contains("terms") || url.contains("legal"));

        if (page.equals("Privacy"))
            assertTrue(url.contains("privacy"));

        BaseClass.driver.close();
        BaseClass.driver.switchTo().window(mainWindow);
    }

    // ---------------- UI ----------------

    @Then("je dois voir le bloc d'accueil avec image")
    public void verify_image() {
        assertTrue(locator.getLocator("_cssSelector_background_image") != null);
    }

    @Then("je dois voir le logo de la marque")
    public void verify_logo() {
        assertTrue(locator.getLocator("_cssSelector_brand_logo").isDisplayed());
    }


}