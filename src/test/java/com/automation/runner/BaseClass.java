package com.automation.runner;

import com.automation.utils.Colors;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class BaseClass {

    public static WebDriver driver;
    public static Properties prop;
    public static Properties locators;
    public static String path_locators = "//src//test//resources//inputs//locators.properties";

    public void start() throws IOException {
        loadConfig();
        runApp();
        driverConfig();
        loadLocator(BaseClass.path_locators);

    }

    public void stop() {

        driver.quit();
    }


    private void runApp() {
        String browserName = prop.getProperty("browser").trim().toLowerCase();

        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions());
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(getEdgeOptions());
                break;
            default:
                try {
                    throw new Exception("Error: Browser name is incorrect ...[Chrome, Edge] are supported \n you are using: " + prop.getProperty("browser"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        System.out.println("Browser: " + browserName + " On ...");
    }

    private void loadConfig() {
        System.out.println("Loading Configuration ...");
        Date currentTime = new Date();
        // Print current time
        System.out.println(Colors.CYAN_CONSOLE_COLOR + "Current time in the server: " + currentTime + Colors.RESET_CONSOLE_COLOR);

        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir") + "//config.properties");
            prop.load(ip);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLocator(String path) {
        System.out.println("Loading Locator ...");
        try {
            locators = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir") + path);
            locators.load(ip);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void driverConfig() {
        System.out.println("Driver Configuration...");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(prop.getProperty("implicit_wait"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(prop.getProperty("page_load_time"))));

    }

    public void connectUrl(String url) {
        System.out.println("Connect to : " + url);
        driver.get(url);
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless=new");
        options.addArguments("--enable-javascript");
        options.addArguments("--disable-gpu");
        options.addArguments("--profile-directory=Default");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=3,840,1080");
        // System.setProperty("webdriver.chrome.driver", "C:/chromedriver-win64/chromedriver.exe");
        return options;
    }

    private EdgeOptions getEdgeOptions() {

        return new EdgeOptions();
    }

}
