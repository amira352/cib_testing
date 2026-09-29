package tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutStepOnePage;
import pages.InventoryPage;
import pages.loginPage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class CartTests {

    private WebDriver driver;
    private loginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setUp() {
        driver = new SafariDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/");

        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);
        inventoryPage.clickAddToCart("Sauce Labs Backpack");
        inventoryPage.clickAddToCart("Sauce Labs Bike Light");

        // Navigate to Cart and initialize cartPage reference
        cartPage = inventoryPage.clickCart();
    }

    @Test
    public void verifyCartPage() {
        Assert.assertEquals(cartPage.getCartTitle(), "Your Cart");
    }

    @Test
    public void verifyItemsAddedToCart() {
        Assert.assertEquals(cartPage.getNumberOfItems(), 2);
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"));
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Bike Light"));
    }

    @Test
    public void removeItemFromCart() {
        cartPage.removeItem("Sauce Labs Backpack");

        Assert.assertEquals(cartPage.getNumberOfItems(), 1);
        Assert.assertFalse(cartPage.isItemInCart("Sauce Labs Backpack"));
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Bike Light"));
    }

    @Test
    public void continueShopping() {
        InventoryPage returnedInventory = cartPage.continueShopping();
        Assert.assertEquals(returnedInventory.getPageTitle(), "Products");
    }

    @Test
    public void proceedToCheckout() {
        CheckoutStepOnePage checkoutPage = cartPage.checkout();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File("./screenshots/" + result.getName() + ".png");
            try {
                FileUtils.copyFile(source, destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }
}