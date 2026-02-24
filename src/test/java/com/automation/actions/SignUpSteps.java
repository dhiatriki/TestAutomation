package com.automation.actions;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;
import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import com.automation.utils.TestEmail; // utility to fetch the verification code
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

public class SignUpSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/InputData.xlsx");
    private Properties dataProp = new Properties();

    private String email;
    private String password;
    private String firstname;
    private String phone;
    private String code;

    private WebDriverWait wait = new WebDriverWait(BaseClass.driver, Duration.ofSeconds(10));
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
    public void open_signup_page() throws InterruptedException {
        BaseClass.driver.get(BaseClass.prop.getProperty("signupUrl"));
        System.out.println("[INFO] Sign up page opened.");
        Thread.sleep(500);
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
    public void enter_signup_email() throws InterruptedException {
        locator.getLocator("_xpath_signup_email").clear();
        locator.getLocator("_xpath_signup_email").sendKeys(email);
        System.out.println("[INFO] Entered email: " + email);
        Thread.sleep(500);
    }

    @When("enter the password for sign up")
    public void enter_signup_password() throws InterruptedException {
        locator.getLocator("_xpath_signup_password").clear();
        locator.getLocator("_xpath_signup_password").sendKeys(password);
        System.out.println("[INFO] Entered password.");
        Thread.sleep(500);

    }

    @When("confirm the password")
    public void confirm_signup_password() throws InterruptedException {
        locator.getLocator("_xpath_signup_confirm_password").clear();
        locator.getLocator("_xpath_signup_confirm_password").sendKeys(password);
        System.out.println("[INFO] Confirmed password.");
        Thread.sleep(500);

    }

    @When("submit the sign up form")
    public void submit_signup() throws InterruptedException {
        locator.getLocator("_xpath_signup_button").click();
        System.out.println("[INFO] Sign up form submitted.");

    }

    // ---------------- EMAIL VERIFICATION ----------------

    @Then("see the email verification page")
    public void verify_email_verification_page() throws InterruptedException {
        sleep(10000); // wait for verification page to load
        WebElement alert = locator.getLocator("_xpath_verification_alert");
        String alertText = alert.getText().trim();
        boolean isCorrect = alertText.equals("Code de vérification") || alertText.equals("Verification code");
        assertTrue("Expected alert text for verification code, found: " + alertText, isCorrect);
        System.out.println("[INFO] Email verification page visible.");
    }

    @When("enter the verification code received by email")
    public void enter_verification_code() throws InterruptedException {
        sleep(5000); // wait for input field
        String code = TestEmail.getVerificationCode(); // fetch code
        WebElement codeField = locator.getLocator("_xpath_verification_field");
        codeField.clear();
        codeField.sendKeys(code);
        System.out.println("[INFO] Entered verification code: " + code);
    }

    @And("submit the code")
    public void submit_verification_code() throws InterruptedException {
        sleep(2000);
        locator.getLocator("_xpath_signup_button").click();
        System.out.println("[INFO] Verification code submitted.");
    }

    // ---------------- PHONE VERIFICATION ----------------

    @When("enter the signup personal information")
    public void enter_signup_personal_info() {
        // Firstname
        WebElement firstNameField = locator.getLocator("_xpath_signup_firstname");
        firstNameField.clear();
        firstNameField.sendKeys(firstname);
        System.out.println("[INFO] Entered firstname: " + firstname);

        // Lastname (from Excel)
        WebElement lastNameField = locator.getLocator("_xpath_signup_lastname");
        String lastname = excelUtils.getLastname();  // assuming you add getLastname() in ExcelUtils
        lastNameField.clear();
        lastNameField.sendKeys(lastname);
        System.out.println("[INFO] Entered lastname: " + lastname);

        // Phone (from data.properties)
        WebElement phoneField = locator.getLocator("_xpath_signup_phone");
        phoneField.clear();
        phoneField.sendKeys(phone);
        System.out.println("[INFO] Entered phone: " + phone);
    }

    @And("submit the personal information")
    public void submit_signup_personal_info() throws InterruptedException {
        // Assuming there’s a submit button; reuse signup button if same
        sleep(2000);
        locator.getLocator("_xpath_signup_button").click();
        System.out.println("[INFO] Submitted personal information.");
    }

    @Then("enter the phone verification code and submit")
    public void enterPhoneVerificationCodeAndSubmit() {

        // Enter the code into the input field
        WebElement codeField = locator.getLocator("_xpath_signup_phone_code");
        codeField.clear();
        codeField.sendKeys(code);

        // Click the submit button
        WebElement submitButton = locator.getLocator("_xpath_signup_button");
        submitButton.click();
    }
    // ---------------- SUCCESS ----------------

    @Then("verify the welcome message after phone verification")
    public void verifyWelcomeMessage() throws InterruptedException {
        sleep(10000);
        WebElement welcomeElement = locator.getLocator("_xpath_signup_welcome");
        String welcomeText = welcomeElement.getText().trim();

        // Assert that the text matches either English or French
        boolean isCorrect = welcomeText.equals("Welcome test to CG Direct!") ||
                welcomeText.equals("Bienvenue test à CG Direct!");

        assertTrue("Welcome message is incorrect! Found: " + welcomeText, isCorrect);

    }


    // ---------------- FOOTER LINKS ----------------
    @Then("the signup footer link {string} should redirect to {string}")
    public void test_footer(String link, String page) throws InterruptedException {
        System.out.println("[INFO] Testing footer link: " + link);

        // Sauvegarder le handle de la fenêtre principale
        String mainWindow = BaseClass.driver.getWindowHandle();

        // Sleep avant de cliquer pour plus de visibilité
        Thread.sleep(1000);

        // Cliquer sur le lien du footer
        switch (link) {
            case "Terms of Use":
                locator.getLocator("xpath_terms_of_use").click();
                break;
            case "Privacy Policy":
                locator.getLocator("_xpath_privacy").click();
                break;
            default:
                throw new RuntimeException("Footer link non géré: " + link);
        }

        // Sleep après le clic pour attendre le rendu de la nouvelle fenêtre
        Thread.sleep(1000);

        // Attendre l'ouverture de la nouvelle fenêtre
        WebDriverWait wait = new WebDriverWait(BaseClass.driver, Duration.ofSeconds(5));
        wait.until(driver -> driver.getWindowHandles().size() > 1);

        // Passer à la nouvelle fenêtre
        for (String w : BaseClass.driver.getWindowHandles()) {
            if (!w.equals(mainWindow)) {
                BaseClass.driver.switchTo().window(w);
                break;
            }
        }

        // Sleep pour s'assurer que la page est complètement chargée
        Thread.sleep(1000);

        // Vérifier l'URL de redirection
        String url = BaseClass.driver.getCurrentUrl().toLowerCase();
        System.out.println("[INFO] Footer redirected URL: " + url);

        if (page.equals("Terms")) {
            assertTrue(url.contains("terms") || url.contains("legal"));
        } else if (page.equals("Privacy")) {
            assertTrue(url.contains("privacy"));
        } else {
            throw new RuntimeException("Page non gérée pour assertion: " + page);
        }

        // Fermer la nouvelle fenêtre et revenir à la principale
        BaseClass.driver.close();
        BaseClass.driver.switchTo().window(mainWindow);

        // Sleep final pour stabiliser avant la prochaine action
        Thread.sleep(1000);
    }

    // ---------------- UI ----------------
    @Then("the home sign up block with image should be visible")
    public void verify_image() {
        assertTrue(locator.getLocator("xpath_signup_background_image") != null);
        System.out.println("[INFO] Home block with image is visible.");
    }

    @Then("the brand logo sign up should be visible")
    public void verify_logo() {
        assertTrue(locator.getLocator("xpath_brand_logo_Sign_up").isDisplayed());
        System.out.println("[INFO] Brand logo is visible.");
    }



    @When("the signup language is switched to French")
    public void switch_to_french() {
        locator.getLocator("_xpath_switchToFrench").click();
        wait.until(ExpectedConditions.textToBePresentInElement(
                locator.getLocator("_className_signup_title"),
                "Commençons"));
    }

    @Then("the signup title should be {string}")
    public void verify_title(String expected) {
        String actual = locator.getLocator("_className_signup_title").getText().trim();
        System.out.println("[INFO] Verifying title: " + actual);
        assertTrue("Expected title: " + expected + " but found: " + actual, actual.equals(expected));
    }
    @Then("the signup slogan should be {string}")
    public void verify_slogan(String expectedSlogan) {
        String actual = locator.getLocator("_className_signup_slogan").getText().trim();
        System.out.println("[INFO] Verifying slogan: " + actual);

        assertTrue(
                "Expected slogan: " + expectedSlogan + " but found: " + actual,
                actual.equalsIgnoreCase(expectedSlogan)
        );
    }

    @Then("the signup  text should be {string}")
    public void verify_login_text(String expected) {
        String actual = locator.getLocator("_xpath_signup_login_text").getText().trim();
        System.out.println("[INFO] Verifying login text: " + actual);
        assertTrue("Expected text: " + expected + " but found: " + actual,
                actual.contains(expected));
    }

    @When("the signup language is switched to English")
    public void switch_signup_to_english() {
        locator.getLocator("_xpath_switchToEnglish").click();

        wait.until(ExpectedConditions.textToBePresentInElement(
                locator.getLocator("_className_signup_title"),
                "Let’s"
        ));

        System.out.println("[INFO] Signup switched to English.");
    }
}