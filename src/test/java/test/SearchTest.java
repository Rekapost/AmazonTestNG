package test;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import base.BaseClass; // Ensure this import is correct and the BaseClass exists in the base package
import pageObjects.SearchPage;

public class SearchTest extends BaseClass {
    private SearchPage searchPage;
    public static String firstProductName;  // Static variable to store product name
    
    @Test(priority = 0)
    public void openHomePage() throws Exception {
        System.out.println("Search Test: Open Home Page");
        searchPage = new SearchPage(driver);
        driver.get("https://www.amazon.in/");
        searchPage.isPageLoaded();
        searchPage.getPageTitle();
    } 

    @Test(priority = 1, dependsOnMethods = "openHomePage")
    public void testAmazonSearch() {
        System.out.println("Search Test: Test Amazon Search");
        firstProductName =searchPage.searchProductAndOpenResultPage("Mobiles");  
    }
    
    @Test(priority = 2, dependsOnMethods = "testAmazonSearch")
    public void verifyProductInSearchResult() {
        System.out.println("Search Test: Verify Product In Search Result");
        System.out.println(" Search Test: Moving to product window ");
        searchPage.moveToProductWindow();    
    }
}
