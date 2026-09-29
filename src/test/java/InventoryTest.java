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
import pages.InventoryPage;
import pages.loginPage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryTest {

    WebDriver driver;
    loginPage login;
    InventoryPage inventory;

    @BeforeMethod
    public void setUp() {
        driver = new SafariDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/");

        login = new loginPage(driver);
        inventory = new InventoryPage(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File("./screenshots/" + result.getName() + ".png");
            try {
                FileUtils.copyFile(source, destination);
                System.out.println("Screenshot captured for defect: " + result.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }

    // 1. Initial State: Ensure cart starts completely empty
    @Test
    public void testCartIsEmptyInitially() {
        login.login("standard_user", "secret_sauce");
        Assert.assertFalse(inventory.isCartBadgeDisplayed(), "Cart badge should not be displayed on initial login.");
        Assert.assertEquals(inventory.getCartItemCount(), 0, "Initial cart count should be 0.");
    }

    // 2. Add multiple items and verify decrements when removing one
    @Test
    public void testAddMultipleItemsAndDecrementCountOnRemoval() {
        login.login("standard_user", "secret_sauce");

        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bike Light";
        String item3 = "Sauce Labs Bolt T-Shirt";

        // Initial check
        Assert.assertEquals(inventory.getCartItemCount(), 0);

        // Add items sequentially and verify increments
        inventory.clickAddToCart(item1);
        Assert.assertEquals(inventory.getCartItemCount(), 1);

        inventory.clickAddToCart(item2);
        Assert.assertEquals(inventory.getCartItemCount(), 2);

        inventory.clickAddToCart(item3);
        int countBeforeRemoval = inventory.getCartItemCount();
        Assert.assertEquals(countBeforeRemoval, 3);

        // Remove one item and check exact decrement
        inventory.clickRemove(item2);
        int countAfterRemoval = inventory.getCartItemCount();

        Assert.assertEquals(countAfterRemoval, countBeforeRemoval - 1, "Cart count did not decrement by 1.");
        Assert.assertTrue(inventory.isAddToCartButtonDisplayed(item2), "Removed item button should reset to 'Add to cart'.");
        Assert.assertTrue(inventory.isRemoveButtonDisplayed(item1), "Unremoved item should still have 'Remove' button.");
    }

    // 3. Sorting Scenario: Name (Z to A)
    @Test
    public void testSortByNameZToA() {
        login.login("standard_user", "secret_sauce");

        inventory.selectSortOption("Name (Z to A)");

        List<String> actualNames = inventory.getAllProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder());

        Assert.assertEquals(actualNames, expectedNames, "Products are not sorted Z to A.");
    }

    // 4. Sorting Scenario: Price (low to high)
    @Test
    public void testSortByPriceLowToHigh() {
        login.login("standard_user", "secret_sauce");

        inventory.selectSortOption("Price (low to high)");

        List<Double> actualPrices = inventory.getAllProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices, "Products are not sorted by Price (low to high).");
    }

    // 5. Defect Scenario: problem_user cannot remove an item from the cart
    @Test
    public void testProblemUserCannotRemoveItemDefect() {
        login.login("problem_user", "secret_sauce");
        String item = "Sauce Labs Backpack";

        inventory.clickAddToCart(item);
        inventory.clickRemove(item);

        // This assertion fails on problem_user, triggering the screenshot in tearDown()
        Assert.assertTrue(
                inventory.isAddToCartButtonDisplayed(item),
                "DEFECT: problem_user cannot remove item from inventory page."
        );
    }
}