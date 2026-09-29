import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CheckoutStepOnePage;
import pages.loginPage;

import java.time.Duration;

public class CheckoutStepOneTest extends BaseTest {

    loginPage loginPage;
    CheckoutStepOnePage checkoutPage;

    @BeforeMethod
    public void openCheckoutStepOne() {
        // Login first, because the checkout page needs a logged-in user
        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // Wait until the login finishes, then open checkout step one directly
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("inventory.html"));
        driver.get("https://www.saucedemo.com/checkout-step-one.html");

        checkoutPage = new CheckoutStepOnePage(driver);
    }

    @Test
    public void validCheckoutInformation() {

        checkoutPage.completeCheckoutInformation("Ahmed", "Ali", "12345");

        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout-step-two.html")
        );
    }

    @Test
    public void emptyFirstName() {

        checkoutPage.completeCheckoutInformation("", "Ali", "12345");
        System.out.println(checkoutPage.getErrorMessage());
        Assert.assertEquals(
                checkoutPage.getErrorMessage(),
                "Error: First Name is required"
        );
    }

    @Test
    public void emptyLastName() {

        checkoutPage.completeCheckoutInformation("Ahmed", "", "12345");
        System.out.println(checkoutPage.getErrorMessage());
        Assert.assertEquals(
                checkoutPage.getErrorMessage(),
                "Error: Last Name is required"
        );
    }

    @Test
    public void emptyPostalCode() {

        checkoutPage.completeCheckoutInformation("Ahmed", "Ali", "");
        System.out.println(checkoutPage.getErrorMessage());
        Assert.assertEquals(
                checkoutPage.getErrorMessage(),
                "Error: Postal Code is required"
        );
    }

    @Test
    public void allFieldsEmpty() {

        checkoutPage.clickContinue();
        System.out.println(checkoutPage.getErrorMessage());
        // The site checks First Name first, so this is the error that shows
        Assert.assertEquals(
                checkoutPage.getErrorMessage(),
                "Error: First Name is required"
        );
    }

    @Test
    public void cancelCheckout() {

        checkoutPage.cancelCheckout();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("cart.html")
        );
    }
}
