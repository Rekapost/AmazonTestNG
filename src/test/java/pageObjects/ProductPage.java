package pageObjects;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
    
    private WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // driver is null here
    JavascriptExecutor js = (JavascriptExecutor) driver; // NullPointerException

    @FindBy(xpath="//input[@id='add-to-cart-button' and @type='submit']")
    WebElement addToCartButton;

    @FindBy(xpath="//div[@id='NATC_SMART_WAGON_CONF_MSG_SUCCESS']/h1")
    WebElement cartMessage;
     
    @FindBy(xpath="//div[@class='attach-dss-modal-layer']//div[@id='attach-desktop-sideSheet']//div[@id='attachDisplayAddBaseAlert']//h4")
    WebElement cartSuccessMessage;

    @FindBy(xpath="//div[@id='attach-accessory-pane'][1]//span[@id='attach-accessory-cart-subtotal']")
    WebElement cartPrice1;

    @FindBy(xpath="//div[@id='sw-subtotal']/span")
    WebElement cartPrice;

    @FindBy(xpath="//div[@id='sw-atc-actions']//span[@id='sw-gtc']//a")
    WebElement goToCart;

    //@FindBy(xpath="//span[@id='attach-sidesheet-view-cart-button']/span[@class='a-button-inner']/input[@class='a-button-input']")

    @FindBy(xpath="//div[@class='a-fixed-left-grid-col a-col-right']//form//input[@type='submit']")
    WebElement cart;
    //div[@class='attach-dss-modal-layer']//div[@id='attach-desktop-sideSheet']//div[@id='attach-added-to-cart-message']//div[@class='a-fixed-left-grid-col a-col-right']//form//input[@type='submit']
    

    public ProductPage(WebDriver driver){
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver is NULL in ProductPage!");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver; // Initialize after assigning driver
        PageFactory.initElements(driver,this);
    }

    public void addToCart(){
        WebElement addCartButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        js.executeScript("arguments[0].scrollIntoView(true);", addCartButton);
        wait.until(ExpectedConditions.visibilityOf(addCartButton));
        //js.executeScript("return document.readyState").equals("complete");
        //js.executeScript("arguments[0].click();", addCartButton);
        addCartButton.click();
    }

    public String getCartSuccessMessage(){
        return cartMessage.getText();
    }

    public String getCartSuccessMessage1(){
        return cartSuccessMessage.getText();
    }
    public String getCartPrice(){
        return cartPrice.getText();
    }
    public String getCartPrice1(){
        return cartPrice1.getText();
    }
   
    public void goToCart(){
        if (driver == null) {
            throw new IllegalStateException("WebDriver instance is null! Cannot proceed.");
        }
        
        System.out.println("Wait for the go to cart button to be clickable before clicking it");
        
        // Wait for the overlay to appear
        WebElement overlay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("attachDisplayAddBaseAlert")));
        System.out.println("Overlay detected.");
        
        // Wait for the side display overlay to appear
        WebElement sideDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("attach-added-to-cart-message")));
        System.out.println("Side display overlay detected.");
        
        try {
            WebElement goToCartButton = wait.until(ExpectedConditions.elementToBeClickable(goToCart));        
            //js.executeScript("arguments[0].click();", goToCartButton);
            goToCartButton.click();
        } catch (TimeoutException e) {        
                WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(cart));              
                //js.executeScript("arguments[0].click();", cartButton);
                cartButton.click();
        }
         System.out.println("Clicked cartButton");  
         // Wait for cart page to load
         wait.until(ExpectedConditions.urlContains("cart"));
         System.out.println("Navigated to cart page: " + driver.getCurrentUrl());
    }

    public void moveToCartWindow(){
        Set<String> windows = driver.getWindowHandles();
        for(String window : windows){
            driver.switchTo().window(window);
            System.out.println("Cart Window Title: " + driver.getTitle());
            System.out.println(driver.getCurrentUrl());
            System.out.println(driver.getWindowHandle());
        }
    }
}
