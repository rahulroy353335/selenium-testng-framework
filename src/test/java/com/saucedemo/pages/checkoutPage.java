package com.saucedemo.pages;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class checkoutPage {

    private WebDriver driver;

    @FindBy(id = "first-name")
    private WebElement firstNameTxt;
    @FindBy(id = "last-name")
    private WebElement lastNameTxt;

    @FindBy(id = "postal-code")
    private WebElement postalCodeTxt;
    @FindBy(id = "continue")
    private WebElement continueBtn;

    public checkoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillCartPage(Map<String, String> cartDetails) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(firstNameTxt)).clear();
            firstNameTxt.sendKeys(cartDetails.get("Firstname"));
            wait.until(ExpectedConditions.visibilityOf(lastNameTxt)).clear();
            lastNameTxt.sendKeys(cartDetails.get("Lastname"));
            wait.until(ExpectedConditions.visibilityOf(postalCodeTxt)).clear();
            postalCodeTxt.sendKeys(cartDetails.get("PostalCode"));
            wait.until(ExpectedConditions.visibilityOf(continueBtn)).click();

        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + e.getMessage());

        }

    }

}
