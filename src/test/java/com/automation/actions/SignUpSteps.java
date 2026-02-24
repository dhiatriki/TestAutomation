package com.automation.actions;

import java.io.FileInputStream;
import java.util.Properties;
import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import com.automation.utils.TestEmail; // utility to fetch the verification code
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

public class SignUpSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/LoginData.xlsx");
    private Properties dataProp = new Properties();

    private String email;
    private String password;
    private String firstname;
    private String phone;
    private String code;
    public SignUpSteps() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/data.properties");
            dataProp.load(fis);
            firstname = dataProp.getProperty("signup.firstname");
            phone = dataProp.getProperty("signup.phone");
            code = dataProp.getProperty("signup.code");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
        System.out.println("[INFO] Sign up form submitted.");
    }

    // ---------------- EMAIL VERIFICATION ----------------

    @Then("see the email verification page")
    public void verify_email_verification_page() throws InterruptedException {
        sleep(10000); // wait for verification page to load
        WebElement alert = locator.getLocator("_cssSelector_verification_alert");
        String alertText = alert.getText().trim();
        boolean isCorrect = alertText.equals("Code de vérification") || alertText.equals("Verification code");
        assertTrue("Expected alert text for verification code, found: " + alertText, isCorrect);
        System.out.println("[INFO] Email verification page visible.");
    }

    @When("enter the verification code received by email")
    public void enter_verification_code() throws InterruptedException {
        sleep(5000); // wait for input field
        String code = TestEmail.getVerificationCode(); // fetch code
        WebElement codeField = locator.getLocator("_cssSelector_verification_field");
        codeField.clear();
        codeField.sendKeys(code);
        System.out.println("[INFO] Entered verification code: " + code);
    }

    @And("submit the code")
    public void submit_verification_code() throws InterruptedException {
        sleep(2000);
        locator.getLocator("_cssSelector_signup_button").click();
        System.out.println("[INFO] Verification code submitted.");
    }

    // ---------------- PHONE VERIFICATION ----------------

    @When("enter the signup personal information")
    public void enter_signup_personal_info() {
        // Firstname
        WebElement firstNameField = locator.getLocator("_cssSelector_signup_firstname");
        firstNameField.clear();
        firstNameField.sendKeys(firstname);
        System.out.println("[INFO] Entered firstname: " + firstname);

        // Lastname (from Excel)
        WebElement lastNameField = locator.getLocator("_cssSelector_signup_lastname");
        String lastname = excelUtils.getLastname();  // assuming you add getLastname() in ExcelUtils
        lastNameField.clear();
        lastNameField.sendKeys(lastname);
        System.out.println("[INFO] Entered lastname: " + lastname);

        // Phone (from data.properties)
        WebElement phoneField = locator.getLocator("_cssSelector_signup_phone");
        phoneField.clear();
        phoneField.sendKeys(phone);
        System.out.println("[INFO] Entered phone: " + phone);
    }

    @And("submit the personal information")
    public void submit_signup_personal_info() throws InterruptedException {
        // Assuming there’s a submit button; reuse signup button if same
        sleep(2000);
        locator.getLocator("_cssSelector_signup_button").click();
        System.out.println("[INFO] Submitted personal information.");
    }

    @Then("enter the phone verification code and submit")
    public void enterPhoneVerificationCodeAndSubmit() {

        // Enter the code into the input field
        WebElement codeField = locator.getLocator("_cssSelector_signup_phone_code");
        codeField.clear();
        codeField.sendKeys(code);

        // Click the submit button
        WebElement submitButton = locator.getLocator("_cssSelector_signup_button");
        submitButton.click();
    }
    // ---------------- SUCCESS ----------------

    @Then("verify the welcome message after phone verification")
    public void verifyWelcomeMessage() throws InterruptedException {
        sleep(10000);
        WebElement welcomeElement = locator.getLocator("_cssSelector_signup_welcome");
        String welcomeText = welcomeElement.getText().trim();

        // Assert that the text matches either English or French
        boolean isCorrect = welcomeText.equals("Welcome test to CG Direct!") ||
                welcomeText.equals("Bienvenue test à CG Direct!");

        assertTrue("Welcome message is incorrect! Found: " + welcomeText, isCorrect);

    }


}