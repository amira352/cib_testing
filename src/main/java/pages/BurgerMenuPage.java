package pages;
import org.openqa.selenium.By;
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
    public void openMenu() {
        driver.findElement(menuButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
    }

    public void closeMenu() {
        driver.findElement(closeButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(logoutLink));
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
