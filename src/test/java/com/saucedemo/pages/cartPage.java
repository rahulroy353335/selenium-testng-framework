package com.saucedemo.pages;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class cartPage {
    private WebDriver driver;

    @FindBy(id = "shopping_cart_container")
    private WebElement cartIcon;

    @FindBy(xpath = "//div[@id='shopping_cart_container']/a/span")
    private WebElement cartCount;

    @FindBy(xpath = "//span[@class='title']")
    private WebElement cartTitle;

    @FindBy(xpath = "//div[@class='cart_item']")
    private WebElement cartItem;

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    public cartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void goCartPage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(cartIcon)).click();
            wait.until(ExpectedConditions.visibilityOf(cartTitle));
            String titleCartPage = cartTitle.getText();
            Assert.assertEquals(titleCartPage, "Your Cart");
            ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + e.getMessage());

        }

    }

    public String checkcartCnt() {
        String cartCnString;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(cartCount));
            cartCnString = cartCount.getText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + e.getMessage());

        }
        return cartCnString;

    }

    public Map<Integer, String> checkCartItems() {

        Map<Integer, String> itemsInCart = new HashMap<>();
        List<WebElement> cartItems = driver.findElements(By.xpath("//div[@class='cart_item']"));
        int count = 1;
        // int cartSize = cartItems.size();

        for (WebElement item : cartItems) {

            WebElement cartElement = item.findElement(
                    By.xpath("//div[@class='inventory_item_name']"));
            String currentCartText = cartElement.getText();

            itemsInCart.put(count, currentCartText);
            count++;
        }

        return itemsInCart;
    }

    public void clickCheckOut() {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(checkoutBtn)).click();

        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + e.getMessage());

        }

    }

}
