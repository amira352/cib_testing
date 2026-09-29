package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductDetailsPage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By productName = By.className("inventory_details_name");
    By productPrice = By.className("inventory_details_price");
    By productDescription = By.className("inventory_details_desc");
    By productImage = By.className("inventory_details_img");
    By addToCartButton = By.id("add-to-cart");
    By removeButton = By.id("remove");
    By backButton = By.id("back-to-products");
    By cartBadge = By.className("shopping_cart_badge");

    // Constructor
    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void openProductFromInventory(String name) {
        driver.findElement(By.xpath(
                "//div[contains(@class,'inventory_item_name') and text()='" + name + "']"
        )).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
    }

    public void openProductById(String id) {
        driver.get("https://www.saucedemo.com/inventory-item.html?id=" + id);
    }

    public void clickAddToCart() {
        driver.findElement(addToCartButton).click();
    }

    public void clickRemove() {
        driver.findElement(removeButton).click();
    }

    public void clickBackToProducts() {
        driver.findElement(backButton).click();
    }

    // Getters
    public String getInventoryPrice(String name) {
        return driver.findElement(By.xpath(
                "//div[text()='" + name + "']/ancestor::div[@class='inventory_item']"
                        + "//div[@class='inventory_item_price']"
        )).getText();
    }

    public String getProductName() {
        return driver.findElement(productName).getText();
    }

    public String getProductPrice() {
        return driver.findElement(productPrice).getText();
    }

    public String getProductDescription() {
        return driver.findElement(productDescription).getText();
    }

    public boolean isProductImageDisplayed() {
        return driver.findElement(productImage).isDisplayed();
    }

    public boolean isAddToCartDisplayed() {
        return !driver.findElements(addToCartButton).isEmpty();
    }

    public boolean isRemoveDisplayed() {
        return !driver.findElements(removeButton).isEmpty();
    }

    public boolean isCartBadgeDisplayed() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    public String getCartBadgeCount() {
        return driver.findElement(cartBadge).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
