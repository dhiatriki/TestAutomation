package com.automation.utils;

import com.automation.runner.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Properties;

public class Locator {
    private final WebDriver driver = BaseClass.driver;
    private final Properties locatorProperties = BaseClass.locators;
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(BaseClass.prop.getProperty("implicit_wait"))));
    private WebElement element = null;

    //xpath
    public WebElement getLocator(String locator_name) {
        String locator = locatorProperties.getProperty(locator_name);
        if (locator != null) {
            String type = getLocatorType(locator_name);
            switch (type) {
                case "xpath":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
                    element = driver.findElement(By.xpath(locator));
                    break;
                case "id":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
                    element = driver.findElement(By.id(locator));
                    break;
                case "name":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locator)));
                    element = driver.findElement(By.name(locator));
                    break;
                case "className":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.className(locator)));
                    element = driver.findElement(By.className(locator));
                    break;
                case "cssSelector":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
                    element = driver.findElement(By.cssSelector(locator));
                    break;
                case "linkText":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locator)));
                    element = driver.findElement(By.linkText(locator));
                    break;
                case "tagName":
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(locator)));
                    element = driver.findElement(By.tagName(locator));
                    break;

            }
            return element;
        } else {
            throw new RuntimeException("Error: No locator found with this name: " + locator_name + " In the locators.properties file");
        }

    }

    public String getLocatorType(String name) {

        if (name.startsWith("_xpath_")) {
            return "xpath";
        }
        if (name.startsWith("_id_")) {
            return "id";
        }
        if (name.startsWith("_name_")) {
            return "name";
        }
        if (name.startsWith("_className_")) {
            return "className";
        }
        if (name.startsWith("_cssSelector_")) {
            return "cssSelector";
        }
        if (name.startsWith("_linkText_")) {
            return "linkText";
        }
        if (name.startsWith("_tagName_")) {
            return "tagName";
        }
        if (locatorProperties.getProperty(name).startsWith("//") || locatorProperties.getProperty(name).startsWith("/")) {
            return "xpath";
        }
        throw new RuntimeException("Error: Syntax name is incorrect: " + name + " name of the locator should start with \n" +
                "[_id] \n" +
                "[_name] \n" +
                "[_className] \n" +
                "[_cssSelector] \n" +
                "[_linkText] \n" +
                "[_tagName] \n" +
                "[_xpath] \n" +
                "[contains // or / for xpath] \n"
        );

    }


}
