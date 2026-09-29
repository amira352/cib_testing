import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.loginPage;

public class LoginTest {

    WebDriver driver;
    loginPage loginPage;

    @BeforeMethod
    public void setUp() {
        driver = new SafariDriver();
        driver.get("https://www.saucedemo.com/");
    }

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

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}