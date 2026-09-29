import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ProductDetailsPage;
import pages.loginPage;

public class ProductDetailsTest extends BaseTest {

    loginPage loginPage;
    ProductDetailsPage detailsPage;

    String backpack = "Sauce Labs Backpack";

    // Helper: login with any user, then init page
    private void loginAs(String username) {
        loginPage = new loginPage(driver);
        loginPage.login(username, "secret_sauce");
        detailsPage = new ProductDetailsPage(driver);
    }

    @Test
    public void openProductShowsCorrectName() {

        loginAs("standard_user");
        detailsPage.openProductFromInventory(backpack);

        Assert.assertEquals(detailsPage.getProductName(), backpack);
    }

    @Test
    public void priceMatchesInventory() {

        loginAs("standard_user");
        String inventoryPrice = detailsPage.getInventoryPrice(backpack);
        detailsPage.openProductFromInventory(backpack);

        Assert.assertEquals(detailsPage.getProductPrice(), inventoryPrice);
    }

    @Test
    public void descriptionAndImageDisplayed() {

        loginAs("standard_user");
        detailsPage.openProductFromInventory(backpack);

        Assert.assertFalse(detailsPage.getProductDescription().isEmpty());
        Assert.assertTrue(detailsPage.isProductImageDisplayed());
    }

    @Test
    public void addToCartFromDetails() {

        loginAs("standard_user");
        detailsPage.openProductFromInventory(backpack);
        detailsPage.clickAddToCart();

        Assert.assertEquals(detailsPage.getCartBadgeCount(), "1");
        Assert.assertTrue(detailsPage.isRemoveDisplayed());
    }

    @Test
    public void removeFromDetails() {

        loginAs("standard_user");
        detailsPage.openProductFromInventory(backpack);
        detailsPage.clickAddToCart();
        detailsPage.clickRemove();

        Assert.assertFalse(detailsPage.isCartBadgeDisplayed());
        Assert.assertTrue(detailsPage.isAddToCartDisplayed());
    }

    @Test
    public void backToProductsNavigatesToInventory() {

        loginAs("standard_user");
        detailsPage.openProductFromInventory(backpack);
        detailsPage.clickBackToProducts();

        Assert.assertTrue(detailsPage.getCurrentUrl().contains("/inventory.html"));
    }

    // Negative: product id that does not exist
    @Test
    public void invalidProductIdShowsNotFound() {

        loginAs("standard_user");
        detailsPage.openProductById("999");
        System.out.println(detailsPage.getProductName());
        Assert.assertTrue(detailsPage.getProductName().contains("ITEM NOT FOUND"));
    }

    // Known defect: problem_user opens a different product than the one clicked
    @Test(description = "DEFECT: problem_user opens wrong product details")
    public void problemUserOpensCorrectProduct() {

        loginAs("problem_user");
        detailsPage.openProductFromInventory(backpack);

        Assert.assertEquals(
                detailsPage.getProductName(),
                backpack,
                "Clicked product and opened product are different"
        );
    }
}
