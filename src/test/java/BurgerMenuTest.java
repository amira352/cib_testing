import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BurgerMenuPage;
import pages.loginPage;

import java.time.Duration;

public class BurgerMenuTest extends BaseTest {

    loginPage loginPage;
    BurgerMenuPage menuPage;

    String backpack = "sauce-labs-backpack";
    String bikeLight = "sauce-labs-bike-light";

    @BeforeMethod
    public void login() {
        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        menuPage = new BurgerMenuPage(driver);
    }

    @Test
    public void menuOpensWithAllItems() {

        menuPage.openMenu();

        Assert.assertTrue(menuPage.areAllMenuItemsDisplayed());
    }

    @Test
    public void menuCloses() {

        menuPage.openMenu();
        menuPage.closeMenu();

        Assert.assertFalse(menuPage.isMenuOpen());
    }

    @Test
    public void allItemsNavigatesToInventory() {

        menuPage.openCart();
        menuPage.clickAllItems();

        Assert.assertTrue(menuPage.getCurrentUrl().contains("/inventory.html"));
        Assert.assertEquals(loginPage.getloginMessage(), "Products");
    }

    @Test
    public void aboutNavigatesToSauceLabs() {

        menuPage.clickAbout();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("saucelabs.com"));

        Assert.assertTrue(menuPage.getCurrentUrl().contains("saucelabs.com"));
    }

    @Test
    public void logoutReturnsToLoginPage() {

        menuPage.clickLogout();

        Assert.assertEquals(menuPage.getCurrentUrl(), "https://www.saucedemo.com/");
    }

    @Test
    public void cannotAccessInventoryAfterLogout() {

        menuPage.clickLogout();
        driver.get("https://www.saucedemo.com/inventory.html");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "You can only access '/inventory.html' when you are logged in"
                )
        );
    }

    @Test
    public void resetAppStateClearsCartBadge() {

        menuPage.addItemToCart(backpack);
        menuPage.addItemToCart(bikeLight);
        Assert.assertEquals(menuPage.getCartBadgeCount(), "2");

        menuPage.clickResetAppState();

        Assert.assertFalse(menuPage.isCartBadgeDisplayed());
    }

    // Known defect: Reset App State clears the cart badge
    // but the "Remove" buttons stay on the inventory page until refresh.
    @Test(description = "DEFECT: Remove buttons not reset after Reset App State")
    public void resetAppStateResetsRemoveButtons() {

        menuPage.addItemToCart(backpack);

        menuPage.clickResetAppState();

        Assert.assertFalse(
                menuPage.isRemoveButtonDisplayed(backpack),
                "Remove button still displayed after Reset App State"
        );
    }
}
