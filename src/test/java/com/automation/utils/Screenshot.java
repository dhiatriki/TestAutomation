package com.automation.utils;

import com.automation.runner.BaseClass;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {

    public static boolean failedScenario = true;
    public static Scenario scenario = null;

    @After
    public void takeScreenshot(Scenario scenario) throws Exception {
        if (scenario.isFailed()) {
            getScreenshot(scenario.getName());
            scenario.attach(getBytScreenshot(), "image/png", scenario.getName());
            ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
            failedScenario = false;
        }

    }

    @Before
    public void skipAllTest(Scenario scenario) {
        Screenshot.scenario = scenario;
        Assume.assumeTrue(failedScenario);
    }

    public static byte[] getBytScreenshot() throws IOException {
        File src = ((TakesScreenshot) BaseClass.driver).getScreenshotAs(OutputType.FILE); 
        return FileUtils.readFileToByteArray(src);

    }

    public String getBase64Screenshot() {
        return ((TakesScreenshot) BaseClass.driver).getScreenshotAs(OutputType.BASE64);
    }

    public static void getScreenshot(String screenshotName) throws Exception {
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) BaseClass.driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "//screenshot//" + screenshotName + " failed " + dateName + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
    }

    public static void getLocalScreenshot(String screenshotName) throws Exception {
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) BaseClass.driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "//screenshot//" + screenshotName + dateName + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
    }

    @Given("^take a screenshot with the name \"(.*)\"$")
    public void takeScreenshot(String name) throws Exception {
        getLocalScreenshot(name);
        scenario.attach(getBytScreenshot(), "image/png", name);
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
    }

}
