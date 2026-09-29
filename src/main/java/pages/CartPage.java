package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {

    WebDriver driver;

    // Locators
    By cartTitle = By.className("title");
    By cartItems = By.className("cart_item");
    By itemNames = By.className("inventory_item_name");
    By removeButtons = By.cssSelector("button[id^='remove-']");
    By continueShoppingButton = By.id("continue-shopping");
    By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getCartTitle() {
        return driver.findElement(cartTitle).getText();
    }

    public int getNumberOfItems() {
        return driver.findElements(cartItems).size();
    }

    public boolean isItemInCart(String itemName) {
        for (WebElement item : driver.findElements(itemNames)) {
            if (item.getText().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(String itemName) {
        String itemId = itemName.toLowerCase().replace(" ", "-");
        driver.findElement(By.id("remove-" + itemId)).click();
    }

    public void removeItem(int index) {
        driver.findElements(removeButtons).get(index).click();
    }

    // Links to InventoryPage
    public InventoryPage continueShopping() {
        driver.findElement(continueShoppingButton).click();
        return new InventoryPage(driver);
    }

    // Links to CheckoutStepOnePage
    public CheckoutStepOnePage checkout() {
        driver.findElement(checkoutButton).click();
        return new CheckoutStepOnePage(driver);
    }
}