package test;
import org.testng.annotations.Test;

import base.BaseClass;
import pageObjects.ProductPage;

public class ProductTest extends BaseClass{
    private ProductPage productPage;
    
    @Test(priority = 3, dependsOnMethods = "test.SearchTest.verifyProductInSearchResult")
    public void testAddToCart() {
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
        System.out.println("Moving to cart page");
        productPage.moveToCartWindow();
    }
}
