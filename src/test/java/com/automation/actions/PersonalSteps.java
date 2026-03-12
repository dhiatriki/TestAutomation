package com.automation.actions;

import com.automation.runner.BaseClass;
import com.automation.utils.ExcelUtils;
import com.automation.utils.Locator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.lang.Thread.sleep;

public class PersonalSteps {

    private final Locator locator = new Locator();
    private final ExcelUtils excelUtils = new ExcelUtils("src/test/resources/InputData.xlsx");
    private final WebDriverWait wait = new WebDriverWait(BaseClass.driver, Duration.ofSeconds(10));
    private static final long SHORT_PAUSE_MS = 350;

    private void pauseShort() throws InterruptedException {
        sleep(SHORT_PAUSE_MS);
    }

    @When("the approval checkbox is clicked")
    public void ClickApprovalCheckbox() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_Chk_approvedRdd_label"))).click();
        System.out.println("[INFO] Disclosure document checkbox selected.");
        pauseShort();
    }

    @And("the RDD start button is clicked")
    public void clickRddStartButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Btn_RddStart"))).click();
        System.out.println("[INFO] RDD start button clicked.");
        pauseShort();
    }

    @And("the accounts are selected")
    public void clickAccounts() throws InterruptedException {
        locator.getLocator("_xpath_cash_account_card").click();
        locator.getLocator("_xpath_tfsa_account_card").click();
        locator.getLocator("_xpath_rrsp_account_card").click();
        pauseShort();
    }

    @And("the open accounts button is clicked")
    public void clickOpenAccountsButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Btn_OpenAccount"))).click();
        System.out.println("[INFO] Open accounts button clicked.");
        pauseShort();
    }

    @And("the personal button is clicked")
    public void clickPersonalButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_id_cardPersonal"))).click();
        System.out.println("[INFO] Personal button clicked.");
        pauseShort();
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) BaseClass.driver).executeScript("arguments[0].click();", element);
        }
    }

    @And("the address information is entered from Excel")
    public void enterAddressInformationFromExcel() throws InterruptedException {
        String streetNumberAndName = excelUtils.getStreetNumberAndName();
        String city = excelUtils.getCity();
        String postalCode = excelUtils.getPostalCode();
        String phone = excelUtils.getPhoneNumber();

        WebElement streetAddressField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_street_address")));
        streetAddressField.clear();
        streetAddressField.sendKeys(streetNumberAndName);

        pauseShort();

        WebElement cityField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_city")));
        cityField.clear();
        cityField.sendKeys(city);

        pauseShort();

        WebElement postalCodeField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_postalCode")));
        postalCodeField.clear();
        postalCodeField.sendKeys(postalCode);

        pauseShort();

        System.out.println("[INFO] Address information entered from Excel.");

        WebElement provinceDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_province_dropdown")));
        ((JavascriptExecutor) BaseClass.driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", provinceDropdown);
        safeClick(provinceDropdown);
        pauseShort();

        WebElement abOption = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_option_AB")));
        ((JavascriptExecutor) BaseClass.driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", abOption);
        safeClick(abOption);
        pauseShort();

        locator.getLocator("_xpath_radio_agree_electronic_delivery").click();
        locator.getLocator("_id_Btn_English").click();
        locator.getLocator("_id_Btn_Woman").click();
        pauseShort();
        locator.getLocator("_id_stepButton").click();
        locator.getLocator("_id_phone").sendKeys(phone);
        sleep(500);
        locator.getLocator("_id_stepButton").click();
        sleep(500);
    }

    @And("the identity and tax information is entered from Excel")
    public void enterIdentityAndTaxInformationFromExcel() throws InterruptedException {
        String dateOfBirth = excelUtils.getDateOfBirth();
        String socialInsuranceNumber = excelUtils.getSocialInsuranceNumber();

        WebElement dobField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_zonetexte")));
        dobField.clear();
        dobField.sendKeys(dateOfBirth);

        pauseShort();

        WebElement socialField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Ipt_Social_field")));
        socialField.clear();
        socialField.sendKeys(socialInsuranceNumber);

        pauseShort();
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_lbl_Canadian"))));

        pauseShort();
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_lbl_TaxResidentCanada"))));

        pauseShort();
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Btn_Single"))));

        pauseShort();
        System.out.println("[INFO] Identity and tax information entered from Excel.");
        pauseShort();
        locator.getLocator("_id_stepButton").click();
        sleep(500);
    }

    @And("the Family member information is entered from Excel")
    public void enterFamilyMemberInformationFromExcel() throws InterruptedException {
        String familyMemberName = excelUtils.getFamilyMemberName();
        String familyMemberLastName = excelUtils.getFamilyMemberLastName();
        String familyMemberAddress = excelUtils.getFamilyMemberAddress();
        String familyMemberPhone = excelUtils.getFamilyMemberPhone();
        String familyMemberMail = excelUtils.getFamilyMemberMail();
        String familymemberCity = excelUtils.getFamilyMemberCity();
        String familymemberProvince = excelUtils.getFamilyMemberProvince();
        String familymemberPostalCode = excelUtils.getFamilyMemberPostalCode();

        WebElement trustedContactYesLabel = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_lbl_trusted_contact_yes")));
        ((JavascriptExecutor) BaseClass.driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", trustedContactYesLabel);
        safeClick(trustedContactYesLabel);
        pauseShort();

        WebElement firstNameField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Ipt_firstName1")));
        firstNameField.clear();
        firstNameField.sendKeys(familyMemberName);

        pauseShort();
        WebElement lastNameField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Ipt_LastName1")));
        lastNameField.clear();
        lastNameField.sendKeys(familyMemberLastName);

        pauseShort();
        WebElement emailField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_Ipt_emailAddress1")));
        emailField.clear();
        emailField.sendKeys(familyMemberMail);

        pauseShort();
        WebElement phoneField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_family_phone")));
        phoneField.clear();
        phoneField.sendKeys(familyMemberPhone);

        pauseShort();
        WebElement relationshipDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_dropdown_relationship")));
        ((JavascriptExecutor) BaseClass.driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", relationshipDropdown);
        safeClick(relationshipDropdown);
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_option_family_member"))));
        pauseShort();

        WebElement countryDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_dropdown_country")));
        ((JavascriptExecutor) BaseClass.driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", countryDropdown);
        safeClick(countryDropdown);
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_option_canada"))));
        pauseShort();

        WebElement streetField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_family_street_address")));
        streetField.clear();
        streetField.sendKeys(familyMemberAddress);

        pauseShort();
        WebElement provinceDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_dropdown_family_province")));
        ((JavascriptExecutor) BaseClass.driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", provinceDropdown);
        safeClick(provinceDropdown);
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_option_mb"))));
        pauseShort();

        System.out.println("[INFO] Family member information entered from Excel.");
        System.out.println("[DEBUG] Family city/province/postal extracted: "
                + familymemberCity + " / " + familymemberProvince + " / " + familymemberPostalCode);

        WebElement cityField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_family_city")));
        cityField.clear();
        cityField.sendKeys(familymemberCity);

        pauseShort();
        WebElement postalCodeField = wait.until(
                ExpectedConditions.elementToBeClickable(locator.getLocator("_id_family_postalCode")));
        postalCodeField.clear();
        postalCodeField.sendKeys(familymemberPostalCode);

        pauseShort();

        sleep(500);
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(locator.getLocator("_xpath_btn_submit"))));
        pauseShort();
    }
}
