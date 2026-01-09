package com.starr.automation.pages;

import com.starr.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base page class containing common page functionality.
 * All page objects should extend this class.
 */
public abstract class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor to initialize page.
     *
     * @param driver WebDriver instance
     */
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be visible.
     *
     * @param locator element locator
     * @return visible WebElement
     */
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable.
     *
     * @param locator element locator
     * @return clickable WebElement
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be invisible.
     *
     * @param locator element locator
     */
    protected void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Click element with wait.
     *
     * @param locator element locator
     */
    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    /**
     * Type text into element with wait.
     *
     * @param locator element locator
     * @param text    text to type
     */
    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element with wait.
     *
     * @param locator element locator
     * @return element text
     */
    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    /**
     * Check if element is displayed.
     *
     * @param locator element locator
     * @return true if element is displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get current page URL.
     *
     * @return current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title.
     *
     * @return page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Check if the page is loaded.
     * Must be implemented by each page object to verify page-specific elements.
     *
     * @return true if page is loaded
     */
    public abstract boolean isLoaded();
}

