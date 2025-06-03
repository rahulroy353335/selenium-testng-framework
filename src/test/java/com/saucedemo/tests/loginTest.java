package com.saucedemo.tests;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.loginPage;
import com.saucedemo.pages.productPage;

public class loginTest extends BaseTest {
    @Test
    public void testValidLogin() {
        loginPage lPage = new loginPage(driver);
        // lPage.login("admin", "password");
        String titlePage = lPage.loginWithJsonCredentials("login_credentials.json");
        test.log(Status.INFO, "Verifying page title");
        test.log(Status.INFO, "Actual title: " + titlePage);

        if (titlePage.contains("Swag Labs")) {
            test.log(Status.PASS, "Title verification passed");
        } else {
            test.log(Status.FAIL, "Title verification failed");
        }

        productPage prodPage = new productPage(driver);
        prodPage.logout();
    }

}
