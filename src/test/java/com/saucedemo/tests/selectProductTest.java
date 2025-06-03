package com.saucedemo.tests;

import java.util.HashMap;

import java.util.Map;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.cartPage;
import com.saucedemo.pages.checkoutPage;
import com.saucedemo.pages.loginPage;
import com.saucedemo.pages.productPage;
import com.saucedemo.utils.JsonDataReader;

public class selectProductTest extends BaseTest {

    @Test(enabled = true)
    public void testSelectProduct() {
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
        Map<String, String> getProducts = JsonDataReader.getCredentials("product.json");
        String item1 = getProducts.get("Item1");
        String item2 = getProducts.get("Item2");
        String item6 = getProducts.get("Item6");
        Map<String, Boolean> itemsToSelect = new HashMap<>();
        itemsToSelect.put(item1, true);
        itemsToSelect.put(item2, true);
        // itemsToSelect.put("Sauce Labs Bolt T-Shirt", false); // won't be selected
        itemsToSelect.put(item6, true);
        test.log(Status.INFO, "Selecting Product Items");
        productPage prodPage = new productPage(driver);
        int prodCnt = prodPage.selectItemByText(itemsToSelect);

        test.log(Status.INFO, +prodCnt + "Items Added in the cart");

        // Going to Cart Page
        cartPage goCart = new cartPage(driver);
        String cartCnt = goCart.checkcartCnt();

        if (cartCnt.equals(Integer.toString(prodCnt))) {
            test.log(Status.PASS, "Items added successfully");
            goCart.goCartPage();
            test.log(Status.PASS, "Navigated to Cart page");
        }
        Map<Integer, String> itemsInCart = goCart.checkCartItems();
        if (itemsInCart.size() == prodCnt) {
            test.log(Status.PASS, "Items displayed successfully");
            goCart.clickCheckOut();
        }

        Map<String, String> cartDetails = new HashMap<>();
        Map<String, String> getName = JsonDataReader.getCredentials("name_form.json");
        cartDetails.put("Firstname", getName.get("firstname"));
        cartDetails.put("Lastname", getName.get("lastName"));
        cartDetails.put("PostalCode", getName.get("postalCode"));
        checkoutPage checkOut = new checkoutPage(driver);
        checkOut.fillCartPage(cartDetails);

    }

    @Test(enabled = false, dependsOnMethods = { "testSelectProduct" })
    public void testCartItems() {

    }
}
