package com.saucedemo.pages;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class productPage {

    private WebDriver driver;
    protected WebDriverWait wait;
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuBtn;

    @FindBy(xpath = "//a[@id='logout_sidebar_link']")
    private WebElement logoutLink;

    @FindBy(xpath = "//a[@id='inventory_sidebar_link']")
    private WebElement allItemsLink;

    @FindBy(xpath = "//a[@id='about_sidebar_link']")
    private WebElement aboutLink;

    @FindBy(xpath = "//a[@id='reset_sidebar_link']")
    private WebElement resetAppLink;

    // Locator for the inventory items container
    @FindBy(how = How.CLASS_NAME, using = "inventory_list")
    private WebElement inventoryList;

    public productPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        menuBtn.click();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

            wait.until(ExpectedConditions.visibilityOf(logoutLink)).click();
            ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + e.getMessage());

        }
        // wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        // logoutLink.click();

    }

    // Method to select an item by its displayed text
    public int selectItemByText(Map<String, Boolean> itemsToSelect) {
        // Find all inventory items
        List<WebElement> items = inventoryList.findElements(By.xpath("./div"));
        int itemsCnt = itemsToSelect.size();
        for (WebElement item : items) {
            // Get the text from the item's title element
            WebElement titleElement = item.findElement(
                    By.xpath(".//div[@class='inventory_item_description']//div[@class='inventory_item_name ']"));
            String currentItemText = titleElement.getText();

            if (itemsToSelect.containsKey(currentItemText) && itemsToSelect.get(currentItemText)) {
                // Click on the item's link if text matches
                WebElement addToCartBtn = item.findElement(
                        By.xpath(
                                ".//div[@class='inventory_item_description']/div/button[@class='btn btn_primary btn_small btn_inventory ']"));

                addToCartBtn.click();
                WebElement RemoveBtnTxt = item.findElement(
                        By.xpath(
                                ".//div[@class='inventory_item_description']/div/button[@class='btn btn_secondary btn_small btn_inventory ']"));
                String removeElementText = RemoveBtnTxt.getText();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                wait.until(ExpectedConditions.visibilityOf(RemoveBtnTxt));
                Assert.assertEquals(removeElementText, "Remove");

            }
        }

        return itemsCnt;
    }

}
