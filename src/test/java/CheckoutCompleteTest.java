import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutCompletePage;
import pages.loginPage;

import java.time.Duration;

public class CheckoutCompleteTest extends BaseTest {

    loginPage loginPage;
    CheckoutCompletePage completePage;

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

    // Helper: login + add item + fill info + finish (reaches Complete page)
    private void placeOrderAs(String username) {
        loginPage = new loginPage(driver);
        loginPage.login(username, "secret_sauce");

        click(By.id("add-to-cart-sauce-labs-backpack"));
        click(By.className("shopping_cart_link"));
        click(By.id("checkout"));

        type(By.id("first-name"), "Test");
        type(By.id("last-name"), "User");
        type(By.id("postal-code"), "12345");
        click(By.id("continue"));

        click(By.id("finish"));

        completePage = new CheckoutCompletePage(driver);
    }

    @Test
    public void completePageUrlAndTitle() {

        placeOrderAs("standard_user");

        Assert.assertTrue(completePage.getCurrentUrl().contains("/checkout-complete.html"));
        Assert.assertEquals(completePage.getPageTitle(), "Checkout: Complete!");
    }

    @Test
    public void thankYouMessageDisplayed() {

        placeOrderAs("standard_user");

        Assert.assertEquals(completePage.getCompleteHeader(), "Thank you for your order!");
    }

    @Test
    public void dispatchTextDisplayed() {

        placeOrderAs("standard_user");
        System.out.println(completePage.getCompleteText());
        Assert.assertTrue(completePage.getCompleteText().contains("Your order has been dispatched"));
    }

    @Test
    public void ponyImageDisplayed() {

        placeOrderAs("standard_user");

        Assert.assertTrue(completePage.isPonyImageDisplayed());
    }

    @Test
    public void cartEmptyAfterOrder() {

        placeOrderAs("standard_user");

        Assert.assertFalse(completePage.isCartBadgeDisplayed());
    }

    @Test
    public void backHomeNavigatesToInventory() {

        placeOrderAs("standard_user");
        completePage.clickBackHome();

        Assert.assertTrue(completePage.getCurrentUrl().contains("/inventory.html"));
    }

    // Negative: open Complete page without login
    @Test
    public void cannotAccessCompletePageWithoutLogin() {

        loginPage = new loginPage(driver);
        driver.get("https://www.saucedemo.com/checkout-complete.html");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "You can only access '/checkout-complete.html' when you are logged in"
                )
        );
    }

    // Known defect: error_user cannot complete the order (Finish does not work)
    @Test(description = "DEFECT: error_user cannot complete the order")
    public void errorUserCanCompleteOrder() {

        placeOrderAs("error_user");

        Assert.assertTrue(
                completePage.getCurrentUrl().contains("/checkout-complete.html"),
                "Order not completed, still on: " + completePage.getCurrentUrl()
        );
    }
}