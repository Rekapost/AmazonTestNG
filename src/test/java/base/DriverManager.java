package base;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import com.aventstack.chaintest.service.ChainPluginService;

public class DriverManager {
    
    private static WebDriver driver;
/*  SLF4J (Simple Logging Facade for Java)
    SLF4J acts as a bridge, meaning your actual logging backend could be Log4j2, Logback, or another logger, depending on your dependencies.
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    public static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
*/

    // Log4j2 (Log4j 2 is a logging framework designed to be fast, flexible, and easy to use)
    public static final Logger logger = LogManager.getLogger(DriverManager.class);
    
    // Singleton design pattern
    private DriverManager()  // Private constructor to prevent direct instantiation
    {

    }
    public static WebDriver getDriver() throws MalformedURLException {
        if (driver == null) { 
             
            // Initialize the Log4j logger 
             PropertyConfigurator.configure("src/test/resources/log4j.properties");
             logger.info("Log4j 1.x Logger initialized.");

            // ChainTest Configuration
            ChainPluginService.getInstance().addSystemInfo("Build#", "1.0");
            ChainPluginService.getInstance().addSystemInfo("Owner Name#", "Reka");    
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36");
            options.addArguments("--disable-logging");
            //chromeOptions.addArguments("--remote-debugging-port=9223"); // Use a custom port
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            //chromeOptions.addArguments("--log-level=3");
            options.addArguments("--remote-allow-origins=*"); 
            options.addArguments("--disable-extensions"); 
            options.addArguments("--disable-gpu");
            options.addArguments("--headless"); 
            // Enabling Browser Logging in Selenium
            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.BROWSER, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);
            
            // Log4j2 Configuration
            logger.info("Logger initialized and logging preferences set.");    
            
        /*  // Use WebDriverManager to download ChromeDriver and set it up
            WebDriverManager.chromedriver().setup(); 
            driver = new ChromeDriver(options);
        */

            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
            } else {
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/ChromeDriver/chromedriver.exe");
            }
            driver = new ChromeDriver(options);

/*       
            // Connect to Selenium Grid
            // Set browser options (Example: Chrome)
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName("chrome");
            capabilities.setCapability("platformName", "LINUX");
            // Merge Options with Capabilities
            capabilities.merge(options);
             // Read the selenium.grid.url property from the command line (set by Maven)
            String gridUrl = System.getProperty("selenium.grid.url", "http://localhost:5555/wd/hub");
            // Initialize RemoteWebDriver with the grid URL and Chrome options
            driver= new RemoteWebDriver(new URL(gridUrl), capabilities);
            //driver = new RemoteWebDriver(new URL("http://localhost:5555/wd/hub"), capabilities);             
*/       
/*
            //String ltUsername = System.getenv("LT_USERNAME");
            //String ltAccessKey = System.getenv("LT_ACCESS_KEY");           
            // Step 1: LambdaTest Capabilities
            Map<String, Object> ltOptions = new HashMap<>();
            ltOptions.put("username", "rekaharisri");
            ltOptions.put("accessKey", "0UV2Eyfkmupm6epnxh6RK6UDtMOebAibFwtZO1WxuPqeySA0zW");
            //ltOptions.put("user", ltUsername); // Fetch from environment
            //ltOptions.put("accessKey", ltAccessKey);       
            ltOptions.put("build", "Amazon-TestNG-Framework");
            ltOptions.put("name", "Amazon Functions");
            ltOptions.put("platformName", "Windows 10");
            ltOptions.put("seCdp", true);
            ltOptions.put("selenium_version", "4.23.0");
            ltOptions.put("geoLocation", "US");

            // Step 2: Add LambdaTest capabilities inside ChromeOptions
            options.setCapability("LT:Options", ltOptions);
            options.setCapability("browserVersion", "latest");
            options.setCapability("platformName", "Windows 10");
            options.setCapability("acceptInsecureCerts", true);

            
            // Initialize RemoteWebDriver with authentication in URL
            //String lambdaUrl = "https://" + ltUsername + ":" + ltAccessKey + "@hub.lambdatest.com/wd/hub";
            //driver = new RemoteWebDriver(new URL(lambdaUrl), options);
            // Initialize RemoteWebDriver with LambdaTest options
            driver= new RemoteWebDriver(new URL("https://hub.lambdatest.com/wd/hub"), options);
*/
            // Wait for the browser to load
            getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS); // Increase timeout
            getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // Longer page load timeout
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            }
            return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
