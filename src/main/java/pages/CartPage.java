package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {

    private WebDriver driver;

    // Locators
    private By cartTitle = By.className("title");
    private By cartItems = By.className("cart_item");
    private By itemNames = By.className("inventory_item_name");
    private By removeButtons = By.cssSelector("button[id^='remove-']");
    private By continueShoppingButton = By.id("continue-shopping");
    private By checkoutButton = By.id("checkout");

    // Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // Get page title
    public String getCartTitle() {
        return driver.findElement(cartTitle).getText();
    }

    // Get number of items in cart
    public int getNumberOfItems() {
        return driver.findElements(cartItems).size();
    }

    // Get item name by index
    public String getItemName(int index) {
        return driver.findElements(itemNames)
                .get(index)
                .getText();
    }

    // Check if item exists in cart
    public boolean isItemInCart(String itemName) {

        for (var item : driver.findElements(itemNames)) {

            if (item.getText().equals(itemName)) {
                return true;
            }
        }

        return false;
    }

    // Remove item by name
    public void removeItem(String itemName) {

        String itemId = itemName
                .toLowerCase()
                .replace(" ", "-");

        By removeButton = By.id("remove-" + itemId);

        driver.findElement(removeButton).click();
    }

    // Remove item by index
    public void removeItem(int index) {
        driver.findElements(removeButtons)
                .get(index)
                .click();
    }

    // Continue shopping
    public InventoryPage continueShopping() {

        driver.findElement(continueShoppingButton).click();

        return new InventoryPage(driver);
    }

    // Go to checkout
    public CheckoutStepOnePage checkout() {

        driver.findElement(checkoutButton).click();

        return new CheckoutStepOnePage(driver);
    }
}