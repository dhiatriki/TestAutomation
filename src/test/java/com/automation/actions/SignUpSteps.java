package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import com.automation.TestEmail; // utility to fetch the verification code
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertTrue;

public class SignUpSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/LoginData.xlsx");

    private String email;
    private String password;

    // ---------------- NAVIGATION ----------------

    @When("open the sign up page")
    public void open_signup_page() {
        BaseClass.driver.get(BaseClass.prop.getProperty("signupUrl"));
        System.out.println("[INFO] Sign up page opened.");
    }

    // ---------------- GET DATA FROM EXCEL ----------------

    @When("get the sign up information from Excel")
    public void read_signup_data() {
        email = excelUtils.getEmail();
        password = excelUtils.getPassword();
        System.out.println("[INFO] Sign up data retrieved from Excel.");
    }

    // ---------------- FILL FORM ----------------

    @When("enter the email for sign up")
    public void enter_signup_email() {
        locator.getLocator("_cssSelector_signup_email").clear();
        locator.getLocator("_cssSelector_signup_email").sendKeys(email);
        System.out.println("[INFO] Entered email: " + email);
    }

    @When("enter the password for sign up")
    public void enter_signup_password() {
        locator.getLocator("_cssSelector_signup_password").clear();
        locator.getLocator("_cssSelector_signup_password").sendKeys(password);
        System.out.println("[INFO] Entered password.");
    }

    @When("confirm the password")
    public void confirm_signup_password() {
        locator.getLocator("_cssSelector_signup_confirm_password").clear();
        locator.getLocator("_cssSelector_signup_confirm_password").sendKeys(password);
        System.out.println("[INFO] Confirmed password.");
    }

    @When("submit the sign up form")
    public void submit_signup() throws InterruptedException {
        locator.getLocator("_cssSelector_signup_button").click();
        Thread.sleep(2000); // small pause for page load
        System.out.println("[INFO] Sign up form submitted.");
    }

    // ---------------- EMAIL VERIFICATION ----------------

    @Then("see the email verification page")
    public void verify_email_verification_page() throws InterruptedException {
        Thread.sleep(2000); // wait for verification page to load
        WebElement alert = locator.getLocator("_cssSelector_verification_alert");
        String alertText = alert.getText().trim();
        boolean isCorrect = alertText.equals("Code de vérification") || alertText.equals("Verification code");
        assertTrue("Expected alert text for verification code, found: " + alertText, isCorrect);
        System.out.println("[INFO] Email verification page visible.");
    }

    @When("enter the verification code received by email")
    public void enter_verification_code() throws InterruptedException {
        Thread.sleep(1000); // wait for input field
        String code = TestEmail.getVerificationCode(); // fetch code
        WebElement codeField = locator.getLocator("_cssSelector_verification_field");
        codeField.clear();
        codeField.sendKeys(code);
        System.out.println("[INFO] Entered verification code: " + code);
    }

    @And("submit the code")
    public void submit_verification_code() {
        locator.getLocator("_cssSelector_verification_field").submit();
        System.out.println("[INFO] Verification code submitted.");
    }

    // ---------------- SUCCESS ----------------

    @Then("account should be created successfully")
    public void verify_signup_success() {
        String text = locator.getLocator("_cssSelector_welcome_message").getText().toLowerCase();
        assertTrue(text.contains("welcome") || text.contains("bienvenue"));
        System.out.println("[INFO] Sign up successful, account created.");
    }
}