package base;
import java.awt.Desktop;
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
import org.testng.annotations.AfterSuite;
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

        @AfterSuite
        public void openHtmlReport(){
            // Generate HTML report here
            try { 
                File htmlReportFile = new File("/target/chaintest/Index.html");
                if(htmlReportFile.exists()){
                    Desktop.getDesktop().browse(htmlReportFile.toURI());
                }else{
                    System.out.println("Report file not found:" + htmlReportFile.getAbsolutePath());
                    }
                }catch(IOException e){
                System.out.println("Report File not found");
            }
       
            //Allure
            try{
                ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/allure", "serve", "allure-results");
                builder.inheritIO();
                Process process= builder.start();
                process.waitFor();

                //allure serve cmd automatically opnes report in browser
                System.out.println("Report File found");
            }catch(Exception e){
                System.out.println("Report File not found");
            }
        }
    }       
