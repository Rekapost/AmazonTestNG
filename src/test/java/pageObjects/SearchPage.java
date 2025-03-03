package pageObjects;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {
    private WebDriver driver;

    @FindBy(xpath="//input[@id='twotabsearchtextbox']")
    WebElement searchField;

    @FindBy(xpath="//form[@id='nav-search-bar-form']//input[@type='submit'][1]")
    WebElement searchButton;  

    @FindBy(xpath="//div[@role='listitem']//h2")
    List<WebElement> searchResults;  // List of all product names on the search page

    @FindBy(xpath="//div[@data-cy='title-recipe']")
    WebElement productName;

    @FindBy(xpath="//div[@data-cy='price-recipe']")
    WebElement productPrice;
    
    @FindBy(xpath="//div[@role='listitem' and @data-index='4']")
    WebElement product4;

    public SearchPage(WebDriver driver){
     this.driver = driver;
     PageFactory.initElements(driver,this);
    }

    public boolean isPageLoaded(){
        System.out.println("Checking the amazon website title");
        return driver.getTitle().contains("Amazon");
    }
    public void submitSearch(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement search = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        search.click();
        System.out.println("Submit Search");
        
    }
    public boolean isSearchFieldDisplayed(){
        System.out.println("Search Field Displayed");
        return searchField.isDisplayed();
    }
    public String getPageTitle(){
        System.out.println("Title: ");
        return driver.getTitle();
    }

   public String getProductName(){
        return productName.getText();
    }

    public void searchForProduct(String product){
        System.out.println("Sending input: ");
        searchField.sendKeys(product);          //Mobiles
        submitSearch();
    }

    public String searchProductAndOpenResultPage(String product){
     searchForProduct(product);
     System.out.println("Number of products found: " + searchResults.size());
     if (!searchResults.isEmpty()) {
        String firstProductName = searchResults.get(2).getText();
        System.out.println("First Product Name: " + firstProductName);       
        searchResults.get(2).click();
        System.out.println("Product Price: " + productPrice.getText());
        System.out.println("Product Name: " + productName.getText());
        System.out.println("Clicked the product");
        return firstProductName;
    } else {
        throw new RuntimeException("No search results found for product: " + product);
    }
    }
    public void moveToProductWindow(){
        Set<String> windows = driver.getWindowHandles();
        for(String window : windows){
        driver.switchTo().window(window);
        System.out.println("Window Title: " + driver.getTitle());
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getWindowHandle());
        }
    }

    public boolean isProductInSearchResult(String product){
     return driver.getPageSource().contains(product);
    }

    public void navigateToPage(String url){
     driver.get(url);
    }
}
