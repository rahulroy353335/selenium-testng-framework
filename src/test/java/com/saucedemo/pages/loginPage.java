package com.saucedemo.pages;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// import com.aventstack.extentreports.MediaEntityBuilder;
import com.saucedemo.utils.*;

public class loginPage {

    private WebDriver driver;

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    public loginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginBtn.click();

    }

    public String loginWithJsonCredentials(String jsonFilename) {
        Map<String, String> getCredentials = JsonDataReader.getCredentials(jsonFilename);

        usernameField.sendKeys(getCredentials.get("username"));
        passwordField.sendKeys(getCredentials.get("password"));

        loginBtn.click();

        String title = driver.getTitle();

        return title;
    }

}
