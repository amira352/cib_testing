import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.FooterPage;
import pages.loginPage;

public class FooterTest extends BaseTest {

    loginPage loginPage;
    FooterPage footerPage;

    @BeforeMethod
    public void login() {
        loginPage = new loginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        footerPage = new FooterPage(driver);
    }

    @Test
    public void footerDisplayedOnInventory() {

        Assert.assertTrue(footerPage.isFooterDisplayed());
    }

    @Test
    public void footerDisplayedOnCartPage() {

        footerPage.openCart();

        Assert.assertTrue(footerPage.isFooterDisplayed());
    }

    @Test
    public void twitterLinkIsCorrect() {

        String href = footerPage.getTwitterHref();
        System.out.println(href);
        Assert.assertTrue(
                (href.contains("twitter.com") || href.contains("x.com")) && href.contains("saucelabs")
        );
    }

    @Test
    public void facebookLinkIsCorrect() {

        Assert.assertTrue(footerPage.getFacebookHref().contains("facebook.com/saucelabs"));
    }

    @Test
    public void linkedinLinkIsCorrect() {

        Assert.assertTrue(footerPage.getLinkedinHref().contains("linkedin.com/company/sauce-labs"));
    }

    @Test
    public void socialLinksOpenInNewTab() {

        Assert.assertEquals(footerPage.getLinkTarget("twitter"), "_blank");
        Assert.assertEquals(footerPage.getLinkTarget("facebook"), "_blank");
        Assert.assertEquals(footerPage.getLinkTarget("linkedin"), "_blank");
    }

    @Test
    public void clickLinkedinOpensNewTab() {

        footerPage.clickLinkedin();
        footerPage.waitForNewTab();

        Assert.assertEquals(footerPage.getOpenTabsCount(), 2);
    }

    @Test
    public void copyrightTextDisplayed() {

        System.out.println(footerPage.getCopyrightText());
        Assert.assertTrue(
                footerPage.getCopyrightText().contains("Sauce Labs. All Rights Reserved")
        );
    }
}