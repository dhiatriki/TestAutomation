package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import io.cucumber.java.en.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private Locator locator = new Locator();
    private ExcelUtils excelUtils = new ExcelUtils("src/test/resources/InputData.xlsx");

    private String email;
    private String password;
    private String mainWindow;

    private WebDriverWait wait = new WebDriverWait(BaseClass.driver, Duration.ofSeconds(10));

    // ---------------- NAVIGATION ----------------
    @Given("the browser is launched")
    public void launch_browser() throws InterruptedException {
        mainWindow = BaseClass.driver.getWindowHandle();
        System.out.println("[INFO] Browser ready or already launched.");
        Thread.sleep(500); // pause pour voir
    }

    @When("the login page is opened")
    public void open_login_page() throws InterruptedException {
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
    public void switch_to_french() throws InterruptedException {
        locator.getLocator("_id_switchToFrench").click();
        wait.until(ExpectedConditions.textToBePresentInElement(locator.getLocator("_xpath_login_title"), "Bienvenue"));
        System.out.println("[INFO] Switched to French.");
        Thread.sleep(500);
    }

    @When("the language is switched to English")
    public void switch_to_english() throws InterruptedException {
        locator.getLocator("_id_switchToEnglish").click();
        wait.until(ExpectedConditions.textToBePresentInElement(locator.getLocator("_xpath_login_title"), "Welcome"));
        System.out.println("[INFO] Switched to English.");
        Thread.sleep(1000);
    }

    // ---------------- TITLE ----------------
    @Then("the title should be {string}")
    public void verify_title(String expected) throws InterruptedException {
        String actual = locator.getLocator("_xpath_login_title").getText().trim();
        System.out.println("[INFO] Verifying title: " + actual);
        assertTrue("Expected title: " + expected + " but found: " + actual, actual.equals(expected));
    }

    // ---------------- LOGIN TEXT ----------------
    @Then("the login text should be {string}")
    public void verify_login_text(String expected) throws InterruptedException {
        String actual = locator.getLocator("_xpath_login_block").getText().trim();
        System.out.println("[INFO] Verifying login text: " + actual);
        assertTrue("Expected text: " + expected + " but found: " + actual, actual.equals(expected));
    }

    // ---------------- SLOGAN ----------------
    @Then("the slogan should be {string}")
    public void verify_slogan(String expectedSlogan) throws InterruptedException {
        String actual = locator.getLocator("_className_brand-slogan").getText().trim();
        System.out.println("[INFO] Verifying slogan: " + actual);
        assertTrue(actual.equals(expectedSlogan));
    }

    // ---------------- LOGIN ----------------
    @When("credentials are retrieved from Excel")
    public void read_credentials() throws InterruptedException {
        email = excelUtils.getEmail();
        password = excelUtils.getPassword();
        System.out.println("[INFO] Credentials retrieved from Excel.");
    }

    @When("an email {string} is entered")
    public void enter_email(String type) throws InterruptedException {
        locator.getLocator("_id_username").clear();
        locator.getLocator("_id_username").sendKeys(type.equals("incorrect") ? email + "1" : email);
        System.out.println("[INFO] Entered email: " + (type.equals("incorrect") ? email + "1" : email));
        Thread.sleep(500);
    }

    @When("a password {string} is entered")
    public void enter_password(String type) throws InterruptedException {
        locator.getLocator("_id_password").clear();
        locator.getLocator("_id_password").sendKeys(type.equals("incorrect") ? password + "1" : password);
        System.out.println("[INFO] Entered password: " + (type.equals("incorrect") ? password + "1" : password));
        Thread.sleep(500);
    }

    @When("the login form is submitted")
    public void click_login() throws InterruptedException {
        locator.getLocator("_cssSelector_login_button").click();
        System.out.println("[INFO] Clicked login button.");
        Thread.sleep(500);
    }

    @Then("the welcome message should be displayed")
    public void verify_success() throws InterruptedException {
        String text = locator.getLocator("_xpath_welcome_message").getText();
        System.out.println("[INFO] Login message: " + text);
        assertTrue(text.contains("Welcome") || text.contains("Bienvenue"));
        excelUtils.incrementCount();
        excelUtils.closeWorkbook();
    }

    @When("the login button is clicked without entering credentials")
    public void click_login_empty() throws InterruptedException {
        locator.getLocator("_cssSelector_login_button").click();
        System.out.println("[INFO] Clicked login with empty fields.");
        Thread.sleep(500);
    }

    @Then("the required field error messages should be displayed")
    public void verify_required_errors() throws InterruptedException {
        String userErr = locator.getLocator("_id_Username-error").getText();
        String passErr = locator.getLocator("_id_Password-error").getText();
        System.out.println("[INFO] User error: " + userErr);
        System.out.println("[INFO] Password error: " + passErr);
        assertTrue(userErr.length() > 0);
        assertTrue(passErr.length() > 0);
        Thread.sleep(1000);
    }

    @Then("the incorrect email or password message should be displayed")
    public void verify_invalid_login() throws InterruptedException {
        String msg = locator.getLocator("_xpath_error_message").getText();
        System.out.println("[INFO] Invalid login message: " + msg);
        assertTrue(msg.length() > 0);
        Thread.sleep(1000);
    }

    // ---------------- FOOTER ----------------
    @Then("the footer link {string} should redirect to {string}")
    public void test_footer(String link, String page) throws InterruptedException {
        System.out.println("[INFO] Testing footer link: " + link);

        switch (link) {
            case "Terms of Use":
                locator.getLocator("xpath_terms_of_use").click();
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
        Thread.sleep(1000);
    }

    // ---------------- UI ----------------
    @Then("the home block with image should be visible")
    public void verify_image() throws InterruptedException {
        assertTrue(locator.getLocator("xpath_background_image") != null);
        System.out.println("[INFO] Home block with image is visible.");
    }

    @Then("the brand logo should be visible")
    public void verify_logo() throws InterruptedException {
        assertTrue(locator.getLocator("_className_brand_logo").isDisplayed());
        System.out.println("[INFO] Brand logo is visible.");
    }

}