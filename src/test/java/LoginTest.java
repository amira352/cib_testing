import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.loginPage;

import java.time.Duration;

public class LoginTest extends BaseTest {

    loginPage loginPage;

    @Test
    public void validLogin() {

        loginPage = new loginPage(driver);

        loginPage.login("standard_user", "secret_sauce");

        Assert.assertEquals(
                loginPage.getloginMessage(),
                "Products"
        );
    }

    @Test
    public void invalidUsername() {

        loginPage = new loginPage(driver);

        loginPage.login("wrong_user", "secret_sauce");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "Epic sadface: Username and password do not match any user in this service"
                )
        );
    }

    @Test
    public void invalidPassword() {

        loginPage = new loginPage(driver);

        loginPage.login("standard_user", "wrong_password");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "Epic sadface: Username and password do not match any user in this service"
                )
        );
    }

    @Test
    public void emptyUsername() {

        loginPage = new loginPage(driver);

        loginPage.login("", "secret_sauce");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "Epic sadface: Username is required"
                )
        );
    }

    @Test
    public void emptyPassword() {

        loginPage = new loginPage(driver);

        loginPage.login("standard_user", "");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "Epic sadface: Password is required"
                )
        );
    }

    // Negative: locked out user must not be able to login
    @Test
    public void lockedOutUser() {

        loginPage = new loginPage(driver);

        loginPage.login("locked_out_user", "secret_sauce");
        System.out.println(loginPage.getErrorMessage());
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "Epic sadface: Sorry, this user has been locked out."
                )
        );
    }

    // Helper: login and return how many seconds it took to reach the Products page
    private double measureLoginSeconds(String username) {
        loginPage = new loginPage(driver);

        long start = System.currentTimeMillis();
        loginPage.login(username, "secret_sauce");
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.urlContains("/inventory.html"));
        double seconds = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println(username + " login time: " + seconds + " seconds");
        return seconds;
    }

    // Performance: normal user should login in less than 3 seconds
    @Test
    public void standardUserLoginIsFast() {

        double seconds = measureLoginSeconds("standard_user");

        Assert.assertTrue(seconds < 3, "Login took " + seconds + " seconds");
    }

    // Known defect: performance_glitch_user takes too long to login
    @Test(description = "DEFECT: performance_glitch_user login is too slow")
    public void performanceGlitchUserLoginIsFast() {

        double seconds = measureLoginSeconds("performance_glitch_user");

        Assert.assertTrue(
                seconds < 3,
                "Login is too slow: took " + seconds + " seconds (expected less than 3)"
        );
    }
}
