import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartBadgePage;
import pages.loginPage;

public class CartBadgeTest extends BaseTest {

    loginPage loginPage;
    CartBadgePage badgePage;

    String backpack = "sauce-labs-backpack";
    String bikeLight = "sauce-labs-bike-light";

    // Helper: login with any user, then init page
    private void loginAs(String username) {
        loginPage = new loginPage(driver);
        loginPage.login(username, "secret_sauce");
        badgePage = new CartBadgePage(driver);
    }

    @Test
    public void badgeNotDisplayedWhenCartEmpty() {

        loginAs("standard_user");

        Assert.assertFalse(badgePage.isBadgeDisplayed());
    }

    @Test
    public void badgeIncreasesOnAdd() {

        loginAs("standard_user");

        badgePage.addItem(backpack);
        Assert.assertEquals(badgePage.getBadgeCount(), "1");

        badgePage.addItem(bikeLight);
        Assert.assertEquals(badgePage.getBadgeCount(), "2");
    }

    @Test
    public void badgeDecreasesOnRemove() {

        loginAs("standard_user");
        badgePage.addItem(backpack);
        badgePage.addItem(bikeLight);

        badgePage.removeItem(backpack);

        Assert.assertEquals(badgePage.getBadgeCount(), "1");
    }

    @Test
    public void badgeDisappearsWhenLastItemRemoved() {

        loginAs("standard_user");
        badgePage.addItem(backpack);

        badgePage.removeItem(backpack);

        Assert.assertFalse(badgePage.isBadgeDisplayed());
    }

    @Test
    public void badgeCountsAllItems() {

        loginAs("standard_user");
        int totalItems = badgePage.getAddButtonsCount();

        badgePage.addAllItems();

        Assert.assertEquals(badgePage.getBadgeCount(), String.valueOf(totalItems));
    }

    @Test
    public void badgePersistsAfterRefresh() {

        loginAs("standard_user");
        badgePage.addItem(backpack);
        badgePage.addItem(bikeLight);

        badgePage.refreshPage();

        Assert.assertEquals(badgePage.getBadgeCount(), "2");
    }

    @Test
    public void badgePersistsOnCartPage() {

        loginAs("standard_user");
        badgePage.addItem(backpack);

        badgePage.openCart();

        Assert.assertTrue(badgePage.getCurrentUrl().contains("/cart.html"));
        Assert.assertEquals(badgePage.getBadgeCount(), "1");
    }

    // Known defect: problem_user cannot add some items, so badge count is wrong
    @Test(description = "DEFECT: problem_user badge does not count all added items")
    public void problemUserBadgeCountsAllItems() {

        loginAs("problem_user");
        int totalItems = badgePage.getAddButtonsCount();

        badgePage.addAllItems();
        System.out.println("Expected: " + totalItems + " | Actual: " + badgePage.getBadgeCount());
        Assert.assertEquals(
                badgePage.getBadgeCount(),
                String.valueOf(totalItems),
                "Badge count does not match number of added items"
        );
    }
}
