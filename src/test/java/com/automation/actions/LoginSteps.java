package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.Locator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    // Initialize Locator utility
    Locator locator = new Locator();

    @Given("je lance le navigateur")
    public void lancer_nav() {
        // Browser is initialized in BaseClass.@BeforeClass
        System.out.println("Browser already launched by BaseClass.");
    }

    @When("j'ouvre la page de login")
    public void open_login_page() {
        // Use the URL from the configuration file instead of hardcoding
        String url = BaseClass.prop.getProperty("url");
        BaseClass.driver.get(url);
    }

    @When("je saisis username {string}")
    public void enter_username(String user) {
        // Locator utility handles explicit waits, so Thread.sleep is unnecessary
        locator.getLocator("_id_username").sendKeys(user);
    }

    @When("je saisis password {string}")
    public void enter_password(String pass) {
        locator.getLocator("_id_password").sendKeys(pass);
    }

    @When("je clique sur login")
    public void click_login() {
        locator.getLocator("_cssSelector_login_button").click();
    }

    @Then("je dois voir le message {string}")
    public void verify_message(String msg) {
        // Locator's wait ensures the element is present before getting text
        String text = locator.getLocator("_id_flash_message").getText();
        assertTrue("Message not found!", text.contains(msg));
    }
}
