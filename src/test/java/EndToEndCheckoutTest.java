import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

public class EndToEndCheckoutTest extends BaseTest {

    private loginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutStepOnePage stepOnePage;
    private CheckoutOverviewPage overviewPage;
    private CheckoutCompletePage completePage;

    /**
     * Scenario A: Positive Flow
     * Login -> Add Product -> Cart -> Fill Valid Details -> Overview -> Complete Order
     */
    @Test(priority = 1, description = "Complete positive checkout journey from login to thank-you page")
    public void testPositiveEndToEndCheckout() {
        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);
        inventoryPage.clickAddToCart("Sauce Labs Backpack");
        cartPage = inventoryPage.clickCart();

        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"));

        stepOnePage = cartPage.checkout();
        stepOnePage.completeCheckoutInformation("Jane", "Doe", "90210");

        overviewPage = new CheckoutOverviewPage(driver);
        Assert.assertTrue(overviewPage.getCurrentUrl().contains("/checkout-step-two.html"));
        Assert.assertEquals(overviewPage.getItemsCount(), 1);

        overviewPage.clickFinish();

        completePage = new CheckoutCompletePage(driver);
        Assert.assertTrue(completePage.getCurrentUrl().contains("/checkout-complete.html"));
        Assert.assertEquals(completePage.getCompleteHeader(), "Thank you for your order!");
        Assert.assertFalse(completePage.isCartBadgeDisplayed());
    }

    /**
     * Scenario B: Negative Flow
     * Login -> Add Product -> Cart -> Checkout -> Submit Missing Information -> Verify Validation
     */
    @Test(priority = 2, description = "Checkout journey stopped by missing mandatory checkout field")
    public void testNegativeCheckoutMissingPostalCode() {
        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);
        inventoryPage.clickAddToCart("Sauce Labs Bike Light");
        cartPage = inventoryPage.clickCart();

        stepOnePage = cartPage.checkout();
        stepOnePage.completeCheckoutInformation("Jane", "Doe", ""); // Missing Postal Code

        Assert.assertEquals(stepOnePage.getErrorMessage(), "Error: Postal Code is required");
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                "User should remain on checkout step one when validation fails.");
    }

    /**
     * Scenario C: Defect Flow
     * Login as problem_user -> Add Product -> Cart -> Checkout -> Attempt to enter Last Name
     * (problem_user has a built-in defect where the Last Name input field is broken/overwritten)
     */
    @Test(priority = 3, description = "DEFECT: problem_user cannot complete checkout due to broken last name input")
    public void testDefectProblemUserCheckoutBlocker() {
        loginPage = new loginPage(driver);
        loginPage.login("problem_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);
        inventoryPage.clickAddToCart("Sauce Labs Backpack");
        cartPage = inventoryPage.clickCart();

        stepOnePage = cartPage.checkout();
        stepOnePage.completeCheckoutInformation("Jane", "Doe", "90210");

        // The defect causes last name input failure, blocking navigation to step two.
        // This assertion fails, catching the defect and capturing a screenshot via BaseTest.
        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout-step-two.html"),
                "DEFECT FOUND: problem_user could not proceed to Overview because Last Name was not accepted."
        );
    }
}