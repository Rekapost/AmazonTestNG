package base;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

public class BaseClass {
protected WebDriver driver; 
protected static final Logger logger = LogManager.getLogger(BaseClass.class);

@BeforeMethod
public void setup(ITestResult result){
    if (driver == null) { 
    try {
        driver = DriverManager.getDriver();
    } catch (MalformedURLException e) {       
    }
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
            String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + testName + ".png";
            //File destinationFile = new File("./target/reports/Screenshots/" + testName + ".png");
            File destinationFile = new File(screenshotPath);   
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured for test: " + testName);
        } catch (IOException e) {
            logger.error("Error capturing screenshot: " + e.getMessage());
        }
    }
}
