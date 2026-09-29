import pages.CartPage;
import pages.InventoryPage;
import pages.loginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTests extends BaseTest {

    private loginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void prepareCart() {

        loginPage loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);

        inventoryPage.clickAddToCart("Sauce Labs Backpack");
        inventoryPage.clickAddToCart("Sauce Labs Bike Light");

        inventoryPage.clickCart();
        cartPage = new CartPage(driver);
    }

    @Test
    public void verifyCartPage() {

        Assert.assertEquals(
                cartPage.getCartTitle(),
                "Your Cart"
        );
    }

    @Test
    public void verifyItemsAddedToCart() {

        Assert.assertEquals(
                cartPage.getNumberOfItems(),
                2
        );

        Assert.assertTrue(
                cartPage.isItemInCart("Sauce Labs Backpack")
        );

        Assert.assertTrue(
                cartPage.isItemInCart("Sauce Labs Bike Light")
        );
    }

    @Test
    public void removeItemFromCart() {

        cartPage.removeItem("Sauce Labs Backpack");

        Assert.assertEquals(
                cartPage.getNumberOfItems(),
                1
        );

        Assert.assertFalse(
                cartPage.isItemInCart("Sauce Labs Backpack")
        );

        Assert.assertTrue(
                cartPage.isItemInCart("Sauce Labs Bike Light")
        );
    }

    @Test
    public void continueShopping() {

        InventoryPage inventoryPage = cartPage.continueShopping();

        Assert.assertEquals(
                inventoryPage.getPageTitle(),
                "Products"
        );
    }

    @Test
    public void proceedToCheckout() {

        cartPage.checkout();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout-step-one")
        );
    }
}