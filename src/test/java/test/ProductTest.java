package test;
import org.testng.annotations.Test;
import utilities.Loggerload;
import base.BaseClass;
import pageObjects.ProductPage;

//@Listeners({AllureTestNg.class, chainTestListener.class, Reporting.class}) 
public class ProductTest extends BaseClass{
    private ProductPage productPage;
    
    @Test(priority = 3, dependsOnMethods = "test.SearchTest.verifyProductInSearchResult")
    public void testAddToCart() {
        utilities.chainTestListener.log("Product Test: Test Add To Cart");
        Loggerload.info("Product Test: Test Add To Cart ");
        System.out.println("Product Test: Test Add To Cart");
        if (driver == null) {
            System.out.println("Driver is NULL in ProductTest! Cannot proceed.");
            return;
        }
    
        productPage = new ProductPage(driver); // Ensure ProductPage gets the correct driver
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getWindowHandle());
        System.out.println(driver.getTitle());
        productPage.addToCart();
    }

    @Test(priority = 4)
    public void testCartSuccessMessage() {
    /*  System.out.println("Product Test:Test Cart Success Message");
        String successMessage= productPage.getCartSuccessMessage(); 
        System.out.println("Cart Success Message: " + successMessage);  
        String successPriceMessage= productPage.getCartPrice(); 
        System.out.println("Cart Price Success Message: " + successPriceMessage);
    */
    try {
        productPage.goToCart();
        } catch (Exception e) {  // Generic catch for other exceptions
        System.out.println("Unexpected error: " + e.getMessage());
        }
        utilities.chainTestListener.log("Product Test: Moving to cart page");
        Loggerload.info("Product Test: Moving to cart page ");
        System.out.println("Product Test: Moving to cart page");
        productPage.moveToCartWindow();
    }
}
