package base;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

public class BaseClass {
protected WebDriver driver;   
//public static WebDriver driver;
public static final Logger logger = LoggerFactory.getLogger(BaseClass.class);

@BeforeMethod
public void setup(){
    if (driver == null) { 
    driver = DriverManager.getDriver();
    }
}

@AfterTest
public void tearDown() {
    //driver.quit();
    DriverManager.quitDriver(); // Close browser only at the end of all tests
}

@AfterMethod
public void attachScreenshot(ITestResult result) {
    if (!result.isSuccess()) {
        captureScreenshot(driver, result.getMethod().getMethodName());
    }else{
        captureScreenshot(driver, result.getMethod().getMethodName());
        System.out.println("Test Passed");
    }
}

    public void captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            System.out.println("Driver is null. Cannot take a screenshot for test: " + testName);
            return;
        }
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File("./target/reports/Screenshots/" + testName + ".png");
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured for test: " + testName);
        } catch (IOException e) {
            logger.error("Error capturing screenshot: " + e.getMessage());
        }
    }
}
