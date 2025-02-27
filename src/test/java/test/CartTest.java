package test;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import base.BaseClass;
import pageObjects.CartPage;

public class CartTest extends BaseClass {
    private CartPage cartPage;
    @Test(priority = 5, dependsOnMethods = "test.ProductTest.testCartSuccessMessage")
    public void testCartItems() {
        System.out.println("Cart Test:Test Cart Items");
        cartPage = new CartPage(driver);
        cartPage.getCartItem(SearchTest.firstProductName);
    }
}
