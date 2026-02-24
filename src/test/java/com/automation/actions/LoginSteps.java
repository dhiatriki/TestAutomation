package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import io.cucumber.java.en.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/LoginData.xlsx");

    private String email;
    private String password;
    private String mainWindow;

    private WebDriverWait wait = new WebDriverWait(BaseClass.driver, Duration.ofSeconds(10));

    // ---------------- NAVIGATION ----------------
    @Given("the browser is launched")
    public void launch_browser() {
        mainWindow = BaseClass.driver.getWindowHandle();
        System.out.println("[INFO] Browser ready or already launched.");
    }

    @When("the login page is opened")
    public void open_login_page() {
        String currentUrl = BaseClass.driver.getCurrentUrl();
        String loginUrl = BaseClass.prop.getProperty("url");

        if (!currentUrl.equals(loginUrl)) {
            BaseClass.driver.get(loginUrl);
            System.out.println("[INFO] Login page opened.");
        } else {
            System.out.println("[INFO] Login page already opened, skipping reload.");
        }
    }

    // ---------------- LANGUAGE ----------------
    @When("the language is switched to French")
    public void switch_to_french() {
        locator.getLocator("_id_switchToFrench").click();
        wait.until(ExpectedConditions.textToBePresentInElement(locator.getLocator("_cssSelector_title"), "Bienvenue"));
        System.out.println("[INFO] Switched to French.");
    }

    @When("the language is switched to English")
    public void switch_to_english() {
        locator.getLocator("_id_switchToEnglish").click();
        wait.until(ExpectedConditions.textToBePresentInElement(locator.getLocator("_cssSelector_title"), "Welcome"));
        System.out.println("[INFO] Switched to English.");
    }

    // ---------------- TITLE ----------------
    @Then("the title should be {string}")
    public void verify_title(String expected) {
        String actual = locator.getLocator("_cssSelector_title").getText().trim();
        System.out.println("[INFO] Verifying title: " + actual);
        assertTrue("Expected title: " + expected + " but found: " + actual, actual.equals(expected));
    }

    // ---------------- SLOGAN ----------------
    @Then("the slogan should be {string}")
    public void verify_slogan(String expectedSlogan) {
        String actual = locator.getLocator("_className_brand-slogan").getText().trim();
        System.out.println("[INFO] Verifying slogan: " + actual);
        assertTrue(actual.equals(expectedSlogan));
    }

    // ---------------- LOGIN TEXT ----------------
    @Then("the login text should be {string}")
    public void verify_login_text(String expected) {
        String actual = locator.getLocator("_cssSelector_login_text").getText().trim();
        System.out.println("[INFO] Verifying login text: " + actual);
        assertTrue("Expected text: " + expected + " but found: " + actual, actual.equals(expected));
    }

    // ---------------- LOGIN ----------------
    @When("credentials are retrieved from Excel")
    public void read_credentials() {
        email = excelUtils.getEmail();
        password = excelUtils.getPassword();
        System.out.println("[INFO] Credentials retrieved from Excel.");
    }

    @When("an email {string} is entered")
    public void enter_email(String type) {
        locator.getLocator("_id_username").clear();
        locator.getLocator("_id_username").sendKeys(type.equals("incorrect") ? email + "1" : email);
        System.out.println("[INFO] Entered email: " + (type.equals("incorrect") ? email + "1" : email));
    }

    @When("a password {string} is entered")
    public void enter_password(String type) {
        locator.getLocator("_id_password").clear();
        locator.getLocator("_id_password").sendKeys(type.equals("incorrect") ? password + "1" : password);
        System.out.println("[INFO] Entered password: " + (type.equals("incorrect") ? password + "1" : password));
    }

    @When("the login form is submitted")
    public void click_login() {
        locator.getLocator("_cssSelector_login_button").click();
        System.out.println("[INFO] Clicked login button.");
    }

    @Then("the welcome message should be displayed")
    public void verify_success() throws InterruptedException {
        sleep(10000);
        String text = locator.getLocator("_cssSelector_welcome_message").getText();
        System.out.println("[INFO] Login message: " + text);
        assertTrue(text.contains("Welcome") || text.contains("Bienvenue"));        // Increment the count in Excel after successful verification
        excelUtils.incrementCount();
        excelUtils.closeWorkbook();
    }

    @When("the login button is clicked without entering credentials")
    public void click_login_empty() {
        locator.getLocator("_cssSelector_login_button").click();
        System.out.println("[INFO] Clicked login with empty fields.");
    }

    @Then("the required field error messages should be displayed")
    public void verify_required_errors() {
        String userErr = locator.getLocator("_id_Username-error").getText();
        String passErr = locator.getLocator("_id_Password-error").getText();
        System.out.println("[INFO] User error: " + userErr);
        System.out.println("[INFO] Password error: " + passErr);
        assertTrue(userErr.length() > 0);
        assertTrue(passErr.length() > 0);
    }

    @Then("the incorrect email or password message should be displayed")
    public void verify_invalid_login() {
        String msg = locator.getLocator("_xpath_error_message").getText();
        System.out.println("[INFO] Invalid login message: " + msg);
        assertTrue(msg.length() > 0);
    }

    // ---------------- FOOTER ----------------
    @Then("the footer link {string} should redirect to {string}")
    public void test_footer(String link, String page) {
        System.out.println("[INFO] Testing footer link: " + link);

        switch (link) {
            case "Terms of Use":
                locator.getLocator("_cssSelector_terms_of_use").click();
                break;
            case "Privacy Policy":
                locator.getLocator("_xpath_privacy").click();
                break;
        }

        // Switch to new tab
        for (String w : BaseClass.driver.getWindowHandles()) {
            if (!w.equals(mainWindow))
                BaseClass.driver.switchTo().window(w);
        }

        String url = BaseClass.driver.getCurrentUrl().toLowerCase();
        System.out.println("[INFO] Footer redirected URL: " + url);

        if (page.equals("Terms"))
            assertTrue(url.contains("terms") || url.contains("legal"));

        if (page.equals("Privacy"))
            assertTrue(url.contains("privacy"));

        BaseClass.driver.close();
        BaseClass.driver.switchTo().window(mainWindow);
    }

    // ---------------- UI ----------------
    @Then("the home block with image should be visible")
    public void verify_image() {
        assertTrue(locator.getLocator("_cssSelector_background_image") != null);
        System.out.println("[INFO] Home block with image is visible.");
    }

    @Then("the brand logo should be visible")
    public void verify_logo() {
        assertTrue(locator.getLocator("_cssSelector_brand_logo").isDisplayed());
        System.out.println("[INFO] Brand logo is visible.");
    }

}