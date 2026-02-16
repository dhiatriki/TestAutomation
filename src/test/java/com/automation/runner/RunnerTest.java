
package com.automation.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/features"},

        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json",
                "junit:target/junit-cucumber-reports/Cucumber-junit.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},

        glue = {"com.automation"},

        stepNotifications = true, useFileNameCompatibleName = true,

        // tags = "@HelloWorld",

        snippets = SnippetType.CAMELCASE)

public class RunnerTest {
    static BaseClass baseClass = new BaseClass();

    @BeforeClass
    public static void setUpClass() throws IOException {

        baseClass.start();
    }

    @AfterClass
    public static void afterTest() {

        baseClass.stop();
    }
}
