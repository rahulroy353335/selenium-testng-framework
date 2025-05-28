package com.example.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
//import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.example.utils.ExtentManager;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
//import java.util.Base64;
import java.util.Date;

public class BaseTest {
    protected WebDriver driver;
    protected ExtentTest test;
    protected static ExtentReports extent;

    @BeforeSuite
    public void setUpSuite() {
        String suiteName = this.getClass().getSimpleName(); // Or use a custom name
        extent = ExtentManager.getInstance(suiteName);
    }

    @BeforeMethod
    public void setup(ITestResult result) {

        test = extent.createTest(result.getMethod().getMethodName());
        // Auto-configure GeckoDriver (works in Docker/Linux/Windows)
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        // Universal headless mode for CI/Docker
        if (isRunningInCI() || isRunningInDocker()) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        // Windows-specific config (local development only)
        if (isRunningOnWindows() && !isRunningInDocker()) {
            options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        }

        driver = new FirefoxDriver(options);
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture screenshot as base64
            String screenshotBase64 = captureScreenshotAsBase64();

            // Add screenshot to report with markup
            test.fail("Test Failed",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            // Capture screenshot for passed test if needed
            String screenshotBase64 = captureScreenshotAsBase64();
            test.pass("Test Passed",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
        }

        extent.flush();
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean isRunningInCI() {
        return System.getenv("CI") != null;
    }

    private boolean isRunningInDocker() {
        return System.getenv("DOCKER") != null ||
                new File("/.dockerenv").exists();
    }

    private boolean isRunningOnWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }

    // New method to capture screenshot as Base64
    public String captureScreenshotAsBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    // Original method to save screenshot to file (optional)
    public String captureScreenshotToFile(String screenshotName) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotPath = System.getProperty("user.dir") + "/test-output/screenshots/" + screenshotName + "_"
                + timeStamp + ".png";

        Path path = Paths.get(System.getProperty("user.dir") + "/test-output/screenshots/");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(srcFile.toPath(), Paths.get(screenshotPath));

        return screenshotPath;
    }
}