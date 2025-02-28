package utilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LambdaTestUtility {
	
	private static final String HUB_URL = "https://hub.lambdatest.com/wd/hub";
	private static final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
	private static final ThreadLocal<DesiredCapabilities> capabilitiesLocal = new ThreadLocal<>();
	
	public static WebDriver initializeLambdaTestSession(String browser,String testName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName",browser);
        capabilities.setCapability("PlatformName", "Windows 10");
        capabilities.setCapability("browserVersion", "127");
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", "rekaharisri");
        ltOptions.put("accessKey", "0UV2Eyfkmupm6epnxh6RK6UDtMOebAibFwtZO1WxuPqeySA0zW"); 
        ltOptions.put("build", "Selenium 4");
        ltOptions.put("name",testName);
        ltOptions.put("platformName", "Windows 10");
        ltOptions.put("seCdp", true);
        ltOptions.put("selenium_version", "4.23.0");
        ltOptions.put("geoLocation", "US"); // Set to your desired location
        capabilities.setCapability("LT:Options", ltOptions);
        capabilitiesLocal.set(capabilities);
        capabilities.setCapability("acceptInsecureCerts", true);

        WebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL(HUB_URL), capabilitiesLocal.get());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			
		}
        driverLocal.set(driver);
	    return driverLocal.get();
	}
    public static void quitSession() {
    	if(driverLocal.get()!=null) {
    		driverLocal.get().quit();
    	}
    }
}