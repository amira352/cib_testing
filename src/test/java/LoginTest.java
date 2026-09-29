import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
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
        driver = new ChromeDriver();
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

        Assert.assertTrue(
                loginPage.getloginMessage().contains(
                        "Epic sadface: Username and password do not match with any user in this service"
                )
        );
    }

    @Test
    public void invalidPassword() {

        loginPage = new loginPage(driver);

        loginPage.login("standard_user", "wrong_password");

        Assert.assertTrue(
                loginPage.getloginMessage().contains(
                        "Epic sadface: Username and password do not match with any user in this service"
                )
        );
    }

    @Test
    public void emptyUsername() {

        loginPage = new loginPage(driver);

        loginPage.login("", "secret_sauce");

        Assert.assertTrue(
                loginPage.getloginMessage().contains(
                        "Epic sadface: Username is required"
                )
        );
    }

    @Test
    public void emptyPassword() {

        loginPage = new loginPage(driver);

        loginPage.login("standard_user", "");

        Assert.assertTrue(
                loginPage.getloginMessage().contains(
                        "Epic sadface: Password is required"
                )
        );
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}