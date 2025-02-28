package test;
import org.testng.annotations.Test;
import base.BaseClass;
import pageObjects.CartPage;
import utilities.Loggerload;

//@Listeners({AllureTestNg.class, chainTestListener.class, Reporting.class}) 
public class CartTest extends BaseClass {
    private CartPage cartPage;
    @Test(priority = 5, dependsOnMethods = "test.ProductTest.testCartSuccessMessage")
    public void testCartItems() {
        utilities.chainTestListener.log("Cart Test:Test Cart Items");
        Loggerload.info("Cart Test:Test Cart Items ");
        System.out.println("Cart Test:Test Cart Items");
        cartPage = new CartPage(driver);
        cartPage.getCartItem(SearchTest.firstProductName);
    }
}
