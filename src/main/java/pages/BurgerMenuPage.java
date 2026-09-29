package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BurgerMenuPage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By menuButton = By.id("react-burger-menu-btn");
    By closeButton = By.id("react-burger-cross-btn");
    By allItemsLink = By.id("inventory_sidebar_link");
    By aboutLink = By.id("about_sidebar_link");
    By logoutLink = By.id("logout_sidebar_link");
    By resetLink = By.id("reset_sidebar_link");
    By cartBadge = By.className("shopping_cart_badge");
    By cartIcon = By.className("shopping_cart_link");

    // Constructor
    public BurgerMenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    // Retry up to 3 times: if the page is still loading, the first click may be ignored
    public void openMenu() {
        for (int i = 0; i < 3; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
                return;
            } catch (TimeoutException e) {
                // menu did not open, try again
            }
        }
        throw new TimeoutException("Burger menu did not open after 3 attempts");
    }

    public void closeMenu() {
        for (int i = 0; i < 3; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.invisibilityOfElementLocated(logoutLink));
                return;
            } catch (TimeoutException e) {
                // menu did not close, try again
            }
        }
        throw new TimeoutException("Burger menu did not close after 3 attempts");
    }

    public void clickAllItems() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(allItemsLink)).click();
    }

    public void clickAbout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(aboutLink)).click();
    }

    public void clickLogout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public void clickResetAppState() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(resetLink)).click();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }

    public void addItemToCart(String itemId) {
        driver.findElement(By.id("add-to-cart-" + itemId)).click();
    }

    // Getters
    public boolean isMenuOpen() {
        return driver.findElement(logoutLink).isDisplayed();
    }

    public boolean areAllMenuItemsDisplayed() {
        return driver.findElement(allItemsLink).isDisplayed()
                && driver.findElement(aboutLink).isDisplayed()
                && driver.findElement(logoutLink).isDisplayed()
                && driver.findElement(resetLink).isDisplayed();
    }

    public boolean isCartBadgeDisplayed() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    public String getCartBadgeCount() {
        return driver.findElement(cartBadge).getText();
    }

    public boolean isRemoveButtonDisplayed(String itemId) {
        return !driver.findElements(By.id("remove-" + itemId)).isEmpty();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
