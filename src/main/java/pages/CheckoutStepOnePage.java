package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepOnePage {

    WebDriver driver;

    // Locators
    By firstNameField = By.id("first-name");
    By lastNameField = By.id("last-name");
    By postalCodeField = By.id("postal-code");
    By continueButton = By.id("continue");
    By cancelButton = By.id("cancel");
    By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    public void enterPostalCode(String postalCode) {
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    // Links back to CartPage
    public CartPage cancelCheckout() {
        driver.findElement(cancelButton).click();
        return new CartPage(driver);
    }

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public void completeCheckoutInformation(String firstName, String lastName, String postalCode) {
        fillCheckoutInformation(firstName, lastName, postalCode);
        clickContinue();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}