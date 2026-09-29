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

public class CheckoutStepOneTest {

    WebDriver driver;
    loginPage loginPage;
    InventoryPage inventoryPage;
    CartPage cartPage;
    CheckoutStepOnePage checkoutPage;

    @BeforeMethod
    public void setUp() {
        driver = new SafariDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/");

        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);
        cartPage = inventoryPage.clickCart();
        checkoutPage = cartPage.checkout();
    }

    @Test
    public void validCheckoutInformation() {
        checkoutPage.completeCheckoutInformation("Ahmed", "Ali", "12345");
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two.html"));
    }

    @Test
    public void emptyFirstName() {
        checkoutPage.completeCheckoutInformation("", "Ali", "12345");
        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: First Name is required");
    }

    @Test
    public void emptyLastName() {
        checkoutPage.completeCheckoutInformation("Ahmed", "", "12345");
        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: Last Name is required");
    }

    @Test
    public void emptyPostalCode() {
        checkoutPage.completeCheckoutInformation("Ahmed", "Ali", "");
        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: Postal Code is required");
    }

    @Test
    public void allFieldsEmpty() {
        checkoutPage.clickContinue();
        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: First Name is required");
    }

    @Test
    public void cancelCheckout() {
        CartPage returnedCart = checkoutPage.cancelCheckout();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"));
        Assert.assertEquals(returnedCart.getCartTitle(), "Your Cart");
    }

    // Defect Scenario: problem_user cannot type or submit their Last Name
    @Test
    public void testProblemUserCannotSubmitLastNameDefect() {
        driver.get("https://www.saucedemo.com/");
        loginPage.login("problem_user", "secret_sauce");

        inventoryPage.clickCart();
        cartPage.checkout();

        checkoutPage.completeCheckoutInformation("Ahmed", "Ali", "12345");

        // problem_user overwrites or ignores last name, triggering an error message instead of advancing
        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout-step-two.html"),
                "DEFECT: problem_user checkout failed because the Last Name field cannot be populated properly."
        );
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