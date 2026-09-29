package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutOverviewPage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By pageTitle = By.className("title");
    By cartItems = By.className("cart_item");
    By itemName = By.className("inventory_item_name");
    By itemPrice = By.className("inventory_item_price");
    By itemQuantity = By.className("cart_quantity");
    By paymentInfo = By.cssSelector("[data-test='payment-info-value']");
    By shippingInfo = By.cssSelector("[data-test='shipping-info-value']");
    By itemTotal = By.className("summary_subtotal_label");
    By tax = By.className("summary_tax_label");
    By total = By.className("summary_total_label");
    By cancelButton = By.id("cancel");
    By finishButton = By.id("finish");

    // Constructor
    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void clickCancel() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
    }

    public void clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    // Getters
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public int getItemsCount() {
        return driver.findElements(cartItems).size();
    }

    public String getFirstItemName() {
        return driver.findElement(itemName).getText();
    }

    public String getFirstItemPrice() {
        return driver.findElement(itemPrice).getText();
    }

    public String getFirstItemQuantity() {
        return driver.findElement(itemQuantity).getText();
    }

    public String getPaymentInfo() {
        return driver.findElement(paymentInfo).getText();
    }

    public String getShippingInfo() {
        return driver.findElement(shippingInfo).getText();
    }

    // Sum of all item prices shown in the list
    public double getSumOfItemPrices() {
        double sum = 0;
        for (WebElement price : driver.findElements(itemPrice)) {
            sum += toNumber(price.getText());
        }
        return sum;
    }

    public double getItemTotal() {
        return toNumber(driver.findElement(itemTotal).getText());
    }

    public double getTax() {
        return toNumber(driver.findElement(tax).getText());
    }

    public double getTotal() {
        return toNumber(driver.findElement(total).getText());
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // "Item total: $29.99" -> 29.99
    private double toNumber(String text) {
        return Double.parseDouble(text.substring(text.indexOf("$") + 1));
    }
}
