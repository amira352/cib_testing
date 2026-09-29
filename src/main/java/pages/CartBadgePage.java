package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartBadgePage {

    WebDriver driver;

    // Locators
    By cartBadge = By.className("shopping_cart_badge");
    By cartIcon = By.className("shopping_cart_link");
    By allAddButtons = By.cssSelector("button[id^='add-to-cart']");

    // Constructor
    public CartBadgePage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void addItem(String itemId) {
        driver.findElement(By.id("add-to-cart-" + itemId)).click();
    }

    public void removeItem(String itemId) {
        driver.findElement(By.id("remove-" + itemId)).click();
    }

    public void addAllItems() {
        List<String> ids = new ArrayList<>();
        for (WebElement button : driver.findElements(allAddButtons)) {
            ids.add(button.getAttribute("id"));
        }
        for (String id : ids) {
            driver.findElement(By.id(id)).click();
        }
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    // Getters
    public boolean isBadgeDisplayed() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    public String getBadgeCount() {
        return driver.findElement(cartBadge).getText();
    }

    public int getAddButtonsCount() {
        return driver.findElements(allAddButtons).size();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
