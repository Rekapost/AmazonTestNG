package utilities;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrowserUtility {
	private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private WebDriverWait wait;

	public WebDriver getDriver() {
		return driver.get();		
	}

	public BrowserUtility(WebDriver driver) {
		super();
		this.driver.set(driver); // initialize the instance variable driver
		wait = new WebDriverWait(driver, Duration.ofSeconds(30L));
	}

	public BrowserUtility(String browserName) {
        Loggerload.info("Launching Browser ");  // log4j2   logeerload class
		
		if (browserName.equalsIgnoreCase("chrome")) {
			driver.set(new ChromeDriver());
			wait = new WebDriverWait(driver.get(), Duration.ofSeconds(30L));
		} else if (browserName.equalsIgnoreCase("edge")) {
			driver.set(new EdgeDriver());
			wait = new WebDriverWait(driver.get(), Duration.ofSeconds(30L));
		} else {
			Loggerload.info("Invalid Browser Name....Please select Chrome or Edge only");
			System.err.println("Invalid Browser Name....Please select Chrome or Edge only");
		}
	}

/*	public BrowserUtility(Browser browserName, boolean isHeadless) {
		Loggerload.info("Launching Browser for " + browserName);
		if (browserName == Browser.CHROME) {
			if (isHeadless) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless=new");
				options.addArguments("--window-size=1920,1080");
				driver.set(new ChromeDriver(options));
				wait = new WebDriverWait(driver.get(), Duration.ofSeconds(30L));
			} else {
				driver.set(new ChromeDriver());
				wait = new WebDriverWait(driver.get(), Duration.ofSeconds(30L));
			}
		} 
	}
*/
	public void goToWebsite(String url) {
		Loggerload.info("Visiting the website " + url);
		driver.get().get(url);
	}

	public void maximizeWindow() {
		Loggerload.info("Maximizing the browser window");
		driver.get().manage().window().maximize();
	}

	public void clickOn(By locator) {
		Loggerload.info("Finding Element with the locator" + locator);
		// WebElement element = driver.get().findElement(locator); // Find the element
		// !!!
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

		Loggerload.info("Element found and now performing Click");
		element.click();
	}
	
	public void clickOnCheckbox(By locator) {
		Loggerload.info("Finding Element with the locator" + locator);
		// WebElement element = driver.get().findElement(locator); // Find the element
		// !!!
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		Loggerload.info("Element found and now performing Click");
		element.click();
	}
	
	public void clickOn(WebElement element) {
		Loggerload.info("Element found and now performing Click");
		element.click();
	}

	public void enterText(By locator, String textToEnter) {
		Loggerload.info("Finding Element with the locator" + locator);
		// WebElement element = driver.get().findElement(locator);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		Loggerload.info("Element found and now enter text " + textToEnter);
		element.sendKeys(textToEnter);
	}

	public void clearText(By textBoxLocator) {
		Loggerload.info("Finding Element with the locator" + textBoxLocator);
		// WebElement element = driver.get().findElement(textBoxLocator);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(textBoxLocator));
		Loggerload.info("Element found and clearing the text box field");
		element.clear();
	}

	public void selectFromDropdown(By dropDownLocator, String optionToSelect) {
		Loggerload.info("Finding Element with the locator" + dropDownLocator);
		WebElement element = driver.get().findElement(dropDownLocator);
		Select select = new Select(element);
		Loggerload.info("Selecting the Option" + optionToSelect);
		select.selectByVisibleText(optionToSelect);
	}

	public void enterSpecialKey(By locator, Keys keytoEnter) {
		Loggerload.info("Finding Element with the locator" + locator);
		// WebElement element = driver.get().findElement(locator);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		Loggerload.info("Element found and now enter special key " + keytoEnter);
		element.sendKeys(keytoEnter);
	}

	public String getVisibleText(By locator) {
		Loggerload.info("Finding Element with the locator" + locator);
		WebElement element = driver.get().findElement(locator);
		Loggerload.info("Element found and now returning the visible " + element.getText());
		return element.getText();
	}

	public List<String> getAllVisibleText(By locator) {
		Loggerload.info("Finding All Elements with the locator" + locator);
		List<WebElement> elementList = driver.get().findElements(locator);
		Loggerload.info("Elements found and now printing the list of elements");
		List<String> visibleTextList = new ArrayList<>();
		for (WebElement element : elementList) {
			System.out.println(getVisibleText(element));
			visibleTextList.add(getVisibleText(element));
		}
		return visibleTextList;
	}

	public List<WebElement> getAllElements(By locator) {
		Loggerload.info("Finding All Elements with the locator" + locator);
		List<WebElement> elementList = driver.get().findElements(locator);
		Loggerload.info("Elements found and now printing the list of elements");
		return elementList;
	}

	public String getVisibleText(WebElement element) {
		Loggerload.info("Returning the visible Text" + element.getText());
		return element.getText();
	}

	public String takeScreenShot(String name) {
		TakesScreenshot screenshot = (TakesScreenshot) driver.get();
		File screenshotData = screenshot.getScreenshotAs(OutputType.FILE);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
		String timeStamp = format.format(date);
		String Path = "./Screenshots/" + name + " - " + timeStamp + ".png";
		File screenshotFile = new File(Path);
		try {
			FileUtils.copyFile(screenshotData, screenshotFile);
		} catch (IOException e) {			
		}
		return Path;
	}

	public void quit() {
		driver.get().quit();
	}
}