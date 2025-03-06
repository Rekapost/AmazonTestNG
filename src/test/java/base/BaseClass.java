package base;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
//import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {
protected WebDriver driver; 
private String browser;

/*		@BeforeSuite
		public void startDockerGrid() throws IOException, InterruptedException
		{
			Runtime.getRuntime().exec("cmd /c start startDockerGrid.bat");
			Thread.sleep(15000);
		}
*/	
/*		@AfterSuite
		public void stopDockerGrid() throws IOException, InterruptedException
		{
			Runtime.getRuntime().exec("cmd /c start stopDockerGrid.bat");
			Thread.sleep(15000);
			
			Runtime.getRuntime().exec("taskkill /f /im cmd.exe"); // kills all process closes command prompt
		}	
*/
        @BeforeMethod
        public void setup(ITestResult result,ITestContext context){
            // Get the browser from TestNG parameters
            browser = (String) context.getCurrentXmlTest().getParameter("browser");
            System.out.println("Browser :" + browser);
            if (driver == null) { 
                try {
                    driver = DriverManager.getDriver(browser);
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
                } catch (IOException e) {
                }
            }
        }
