import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutOverviewPage;
import pages.loginPage;

import java.time.Duration;

public class CheckoutOverviewTest extends BaseTest {

    loginPage loginPage;
    CheckoutOverviewPage overviewPage;

    String backpack = "sauce-labs-backpack";
    String bikeLight = "sauce-labs-bike-light";

    // Helpers: wait for element, scroll it to the center, then click / type
    private void click(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        element.click();
    }

    private void type(By locator, String text) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(text);
    }

    // Helper: login + add items + fill info + continue (reaches Overview page)
    private void goToOverviewAs(String username, String... items) {
        loginPage = new loginPage(driver);
        loginPage.login(username, "secret_sauce");

        for (String item : items) {
            click(By.id("add-to-cart-" + item));
        }
        click(By.className("shopping_cart_link"));
        click(By.id("checkout"));

        type(By.id("first-name"), "Test");
        type(By.id("last-name"), "User");
        type(By.id("postal-code"), "12345");
        click(By.id("continue"));

        overviewPage = new CheckoutOverviewPage(driver);
    }

    @Test
    public void overviewUrlAndTitle() {

        goToOverviewAs("standard_user", backpack);

        Assert.assertTrue(overviewPage.getCurrentUrl().contains("/checkout-step-two.html"));
        Assert.assertEquals(overviewPage.getPageTitle(), "Checkout: Overview");
    }

    @Test
    public void itemDisplayedWithCorrectDetails() {

        goToOverviewAs("standard_user", backpack);
        overviewPage.getPageTitle();

        Assert.assertEquals(overviewPage.getItemsCount(), 1);
        Assert.assertEquals(overviewPage.getFirstItemName(), "Sauce Labs Backpack");
        Assert.assertEquals(overviewPage.getFirstItemPrice(), "$29.99");
        Assert.assertEquals(overviewPage.getFirstItemQuantity(), "1");
    }

    @Test
    public void paymentAndShippingInfoDisplayed() {

        goToOverviewAs("standard_user", backpack);
        overviewPage.getPageTitle();

        Assert.assertEquals(overviewPage.getPaymentInfo(), "SauceCard #31337");
        Assert.assertEquals(overviewPage.getShippingInfo(), "Free Pony Express Delivery!");
    }

    @Test
    public void itemTotalEqualsSumOfPrices() {

        goToOverviewAs("standard_user", backpack, bikeLight);
        overviewPage.getPageTitle();

        Assert.assertEquals(overviewPage.getItemTotal(), overviewPage.getSumOfItemPrices(), 0.01);
    }

    @Test
    public void taxIsEightPercentOfItemTotal() {

        goToOverviewAs("standard_user", backpack, bikeLight);
        overviewPage.getPageTitle();

        double expectedTax = Math.round(overviewPage.getItemTotal() * 0.08 * 100) / 100.0;
        System.out.println("Expected tax: " + expectedTax + " | Actual: " + overviewPage.getTax());
        Assert.assertEquals(overviewPage.getTax(), expectedTax, 0.01);
    }

    @Test
    public void totalEqualsItemTotalPlusTax() {

        goToOverviewAs("standard_user", backpack, bikeLight);
        overviewPage.getPageTitle();

        double expectedTotal = overviewPage.getItemTotal() + overviewPage.getTax();
        Assert.assertEquals(overviewPage.getTotal(), expectedTotal, 0.01);
    }

    @Test
    public void cancelReturnsToInventory() {

        goToOverviewAs("standard_user", backpack);
        overviewPage.clickCancel();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/inventory.html"));
        Assert.assertTrue(overviewPage.getCurrentUrl().contains("/inventory.html"));
    }

    @Test
    public void finishNavigatesToComplete() {

        goToOverviewAs("standard_user", backpack);
        overviewPage.clickFinish();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/checkout-complete.html"));
        Assert.assertTrue(overviewPage.getCurrentUrl().contains("/checkout-complete.html"));
    }

    // Negative: open Overview page without login
    @Test
    public void cannotAccessOverviewWithoutLogin() {

        loginPage = new loginPage(driver);
        driver.get("https://www.saucedemo.com/checkout-step-two.html");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "You can only access '/checkout-step-two.html' when you are logged in"
                )
        );
    }

    // Known defect: problem_user cannot fill Last Name, so Overview is never reached
    @Test(description = "DEFECT: problem_user cannot reach Checkout Overview")
    public void problemUserReachesOverview() {

        goToOverviewAs("problem_user", backpack);

        Assert.assertTrue(
                overviewPage.getCurrentUrl().contains("/checkout-step-two.html"),
                "Overview not reached, still on: " + overviewPage.getCurrentUrl()
        );
    }
}
