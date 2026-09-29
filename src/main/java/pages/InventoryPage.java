package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage {

    WebDriver driver;

    // Locators
    By pageTitle = By.className("title");
    By cartBadge = By.className("shopping_cart_badge");
    By cartLink = By.className("shopping_cart_link");
    By sortDropdown = By.className("product_sort_container");
    By itemNames = By.className("inventory_item_name");
    By itemPrices = By.className("inventory_item_price");

    // Dynamic locator helpers
    private By getAddToCartButton(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        return By.id(buttonId);
    }

    private By getRemoveButton(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        return By.id(buttonId);
    }

    // Constructor
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void clickAddToCart(String productName) {
        driver.findElement(getAddToCartButton(productName)).click();
    }

    public void clickRemove(String productName) {
        driver.findElement(getRemoveButton(productName)).click();
    }

    public void clickCart() {
        driver.findElement(cartLink).click();
    }

    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public boolean isAddToCartButtonDisplayed(String productName) {
        return !driver.findElements(getAddToCartButton(productName)).isEmpty();
    }

    public boolean isRemoveButtonDisplayed(String productName) {
        return !driver.findElements(getRemoveButton(productName)).isEmpty();
    }

    public boolean isCartBadgeDisplayed() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    public int getCartItemCount() {
        if (!isCartBadgeDisplayed()) {
            return 0;
        }
        return Integer.parseInt(driver.findElement(cartBadge).getText());
    }

    // Sorting Actions
    public void selectSortOption(String visibleText) {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText(visibleText);
    }

    public List<String> getAllProductNames() {
        List<WebElement> elements = driver.findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText());
        }
        return names;
    }

    public List<Double> getAllProductPrices() {
        List<WebElement> elements = driver.findElements(itemPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement element : elements) {
            // Strip the "$" symbol before parsing to double
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }
}