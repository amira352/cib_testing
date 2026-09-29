package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class loginPage {

    WebDriver driver;

    // Locators
    By usernameField = By.id("user-name");
    By passwordField = By.id("password");
    By loginButton = By.id("login-button");
    By loginMessage = By.cssSelector("#header_container > div.header_secondary_container > span");
By errorMessage = By.xpath("//*[@id=\"login_button_container\"]/div/form/div[3]/h3");
    // Constructor
    public loginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getloginMessage() {
        return driver.findElement(loginMessage).getText();
    }
    public String getErrorMessage(){return driver.findElement(errorMessage).getText();}
}