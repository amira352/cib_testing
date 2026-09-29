package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutCompletePage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By pageTitle = By.className("title");
    By completeHeader = By.className("complete-header");
    By completeText = By.className("complete-text");
    By ponyImage = By.className("pony_express");
    By backHomeButton = By.id("back-to-products");
    By cartBadge = By.className("shopping_cart_badge");

    // Constructor
    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void clickBackHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backHomeButton)).click();
    }

    // Getters
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public String getCompleteHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    public String getCompleteText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeText)).getText();
    }

    public boolean isPonyImageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(ponyImage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isBackHomeDisplayed() {
        return driver.findElement(backHomeButton).isDisplayed();
    }

    public boolean isCartBadgeDisplayed() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}