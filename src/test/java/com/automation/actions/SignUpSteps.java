package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import com.automation.TestEmail; // your utility to fetch the verification code
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

public class SignUpSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/LoginData.xlsx");

    private String email;
    private String password;

    // ---------------- NAVIGATION ----------------

    @When("j'ouvre la page d'inscription")
    public void open_signup_page() {
        BaseClass.driver.get(BaseClass.prop.getProperty("signupUrl"));
    }

    // ---------------- GET DATA FROM EXCEL ----------------

    @When("je récupère les informations d'inscription depuis Excel")
    public void read_signup_data() {
        email = excelUtils.getEmail();
        password = excelUtils.getPassword();
    }

    // ---------------- FILL FORM ----------------

    @When("je saisis l'email pour l'inscription")
    public void enter_signup_email() {
        locator.getLocator("_cssSelector_signup_email").clear();
        locator.getLocator("_cssSelector_signup_email").sendKeys(email);
    }

    @When("je saisis le mot de passe pour l'inscription")
    public void enter_signup_password() {
        locator.getLocator("_cssSelector_signup_password").clear();
        locator.getLocator("_cssSelector_signup_password").sendKeys(password);
    }

    @When("je confirme le mot de passe")
    public void confirm_signup_password() {
        locator.getLocator("_cssSelector_signup_confirm_password").clear();
        locator.getLocator("_cssSelector_signup_confirm_password").sendKeys(password);
    }

    @When("je valide l'inscription")
    public void submit_signup() throws InterruptedException {
        locator.getLocator("_cssSelector_signup_button").click();
        Thread.sleep(2000);
    }


    @When("je saisis le code de vérification reçu par email")
    public void enter_verification_code() throws InterruptedException {
        // Wait in case the input field takes time to appear
        Thread.sleep(1000);

        // Get the code from your TestEmail utility
        String code = TestEmail.getVerificationCode(); // assuming it's a static method returning String

        // Get the verification input field from locator.properties
        WebElement codeField = locator.getLocator("_cssSelector_verification_field");
        codeField.clear();
        codeField.sendKeys(code);

        WebElement signupButton = locator.getLocator("_cssSelector_signup_button");
        signupButton.click();
    }

    @And("je valide le code")
    public void submit_verification_code() {
        // Press Enter or click next button if needed
        // If you have a next button, you can do:
        // WebElement nextButton = Locator.getLocator("_cssSelector_verification_next_button");
        // nextButton.click();
        // Otherwise, we can send Enter
        locator.getLocator("_cssSelector_verification_field").submit();
    }

    // ---------------- VALIDATION ----------------
    @Then("je dois voir la page de vérification de l'email")
    public void verify_email_verification_page() throws InterruptedException {
        Thread.sleep(5000);
        WebElement alert = locator.getLocator("_cssSelector_verification_alert");
        String alertText = alert.getText().trim();
        boolean isCorrect = alertText.equals("Code de vérification") || alertText.equals("Verification code");
        assertTrue(
                "Le texte d'alerte attendu est 'Code de vérification' ou 'Verification code', trouvé: " + alertText,
                isCorrect
        );
    }
    @Then("le compte doit être créé avec succès")
    public void verify_signup_success() {

        String text = locator.getLocator("_cssSelector_welcome_message")
                .getText()
                .toLowerCase();

        assertTrue(text.contains("welcome") || text.contains("bienvenue"));

        // ✅ prepare next unique email for next run
        //excelUtils.incrementCount();
        //excelUtils.closeWorkbook();
    }
}