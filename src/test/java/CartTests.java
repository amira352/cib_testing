import pages.CartPage;
import pages.InventoryPage;
import pages.loginPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTests {

    private WebDriver driver;
    private loginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setUp() {

        driver = new EdgeDriver();

        driver.get("https://www.saucedemo.com/");

        loginPage loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");

        cartPage = inventoryPage.goToCart();
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

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}