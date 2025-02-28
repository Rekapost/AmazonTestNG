package test;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import base.BaseClass;
import pageObjects.SearchPage; 
import utilities.Loggerload;

//@Listeners({AllureTestNg.class, chainTestListener.class, Reporting.class}) 
public class SearchTest extends BaseClass {
    private SearchPage searchPage;
    public static String firstProductName;  // Static variable to store product name
    public static Logger testCaseLogger = Logger.getLogger(SearchTest.class);


    @Test(priority = 0)
    public void openHomePage() throws Exception {
        System.out.println("Search Test: Open Home Page");
        utilities.chainTestListener.log("Search Test: Open Home Page:");
        Loggerload.info("Search Test: Open Home Page: ");        // log4j2   logeerload class
		testCaseLogger.info("Search Test: Open Home Page:");     //log4j  
        searchPage = new SearchPage(driver);
        driver.get("https://www.amazon.in/");
        searchPage.isPageLoaded();
        searchPage.getPageTitle();
    } 

    @Test(priority = 1, dependsOnMethods = "openHomePage")
    public void testAmazonSearch() {
        System.out.println("Search Test: Test Amazon Search");
        utilities.chainTestListener.log("Search Test: Test Amazon Search");      
        Loggerload.info("Search Test: Test Amazon Search");
        firstProductName =searchPage.searchProductAndOpenResultPage("Mobiles");  
    }
    
    @Test(priority = 2, dependsOnMethods = "testAmazonSearch")
    public void verifyProductInSearchResult() {
        System.out.println("Search Test: Verify Product In Search Result");
        System.out.println(" Search Test: Moving to product window ");
        utilities.chainTestListener.log("Search Test: Moving to product window");
        Loggerload.info("Product Test: Moving to cart page ");
        searchPage.moveToProductWindow();    
    }
}
