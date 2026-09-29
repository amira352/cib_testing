package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FooterPage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By footer = By.className("footer");
    By twitterLink = By.cssSelector(".footer a[href*='twitter.com'], .footer a[href*='x.com']");
    By facebookLink = By.cssSelector(".social_facebook a");
    By linkedinLink = By.cssSelector(".social_linkedin a");
    By copyrightText = By.className("footer_copy");
    By cartIcon = By.className("shopping_cart_link");

    // Constructor
    public FooterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void clickTwitter() {
        wait.until(ExpectedConditions.elementToBeClickable(twitterLink)).click();
    }

    public void clickFacebook() {
        wait.until(ExpectedConditions.elementToBeClickable(facebookLink)).click();
    }

    public void clickLinkedin() {
        wait.until(ExpectedConditions.elementToBeClickable(linkedinLink)).click();
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        wait.until(ExpectedConditions.urlContains("/cart.html"));
    }

    public void waitForNewTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
    }

    // Getters
    public boolean isFooterDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(footer)).isDisplayed();
    }

    public String getTwitterHref() {
        return driver.findElement(twitterLink).getAttribute("href");
    }

    public String getFacebookHref() {
        return driver.findElement(facebookLink).getAttribute("href");
    }

    public String getLinkedinHref() {
        return driver.findElement(linkedinLink).getAttribute("href");
    }

    public String getLinkTarget(String social) {
        By link = social.equals("twitter") ? twitterLink : By.cssSelector(".social_" + social + " a");
        return driver.findElement(link).getAttribute("target");
    }

    public String getCopyrightText() {
        return driver.findElement(copyrightText).getText();
    }

    public int getOpenTabsCount() {
        return driver.getWindowHandles().size();
    }
}