package pageObjects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    //@FindBy(xpath="//div[@data-name='Active Items']/div[@data-item-index=2]")       
    
    @FindBy(xpath="//div[@data-name='Active Items']//div[@data-csa-c-type='item']")
    List<WebElement> items;
    
    
    public CartPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public void getCartItem(String expectedProductName){
        boolean itemFound = false;
        for (WebElement item : items) {
            String itemText = item.getText();
            System.out.println("Cart Item: " + itemText);
            if (itemText.contains(expectedProductName)) {
                itemFound = true;
                System.out.println("Item found in cart: " + expectedProductName);
                break;
            }
        }
        if (!itemFound) {
            System.out.println("Item NOT found in cart!");
        }
    }
}

