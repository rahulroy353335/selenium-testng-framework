package com.example;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.example.base.BaseTest;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Test;

public class FirefoxTest extends BaseTest {

    @Test
    public void testGoogle() {
        // Set path to GeckoDriver
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        driver = new FirefoxDriver(options);

        test.log(Status.INFO, "Launching Firefox browser");
        driver.get("https://www.google.com");

        test.log(Status.INFO, "Verifying page title");
        String title = driver.getTitle();
        test.log(Status.INFO, "Actual title: " + title);

        if (title.contains("Google")) {
            test.log(Status.PASS, "Title verification passed");
        } else {
            test.log(Status.FAIL, "Title verification failed");
        }

        // Optional: Take screenshot at specific point in test
        String screenshotBase64 = captureScreenshotAsBase64();
        test.info("Additional screenshot",
                MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
    }
}