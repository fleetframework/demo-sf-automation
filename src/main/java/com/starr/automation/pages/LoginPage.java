package com.starr.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model for Salesforce Login Page.
 * Locators extracted using Playwright MCP analysis.
 */
public class LoginPage extends BasePage {

    // Locators - extracted from Playwright analysis
    private static final By USERNAME_INPUT = By.id("username");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("Login");
    private static final By REMEMBER_ME_CHECKBOX = By.id("rememberUn");
    private static final By FORGOT_PASSWORD_LINK = By.linkText("Forgot Your Password?");
    private static final By ERROR_MESSAGE = By.id("error");
    private static final By SALESFORCE_LOGO = By.cssSelector(".slds-icon-logo");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to login page.
     *
     * @param url login page URL
     */
    public void navigateTo(String url) {
        logger.info("Navigating to login page: {}", url);
        driver.get(url);
    }

    /**
     * Verify login page is loaded.
     *
     * @return true if login page is displayed
     */
    public boolean isLoaded() {
        logger.info("Verifying login page is loaded");
        return isDisplayed(USERNAME_INPUT) && isDisplayed(PASSWORD_INPUT) && isDisplayed(LOGIN_BUTTON);
    }

    /**
     * Enter username.
     *
     * @param username username to enter
     * @return this LoginPage instance for method chaining
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        type(USERNAME_INPUT, username);
        return this;
    }

    /**
     * Enter password.
     *
     * @param password password to enter
     * @return this LoginPage instance for method chaining
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        type(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Click login button.
     */
    public void clickLogin() {
        logger.info("Clicking login button");
        click(LOGIN_BUTTON);
    }

    /**
     * Perform complete login action.
     *
     * @param username username
     * @param password password
     */
    public void login(String username, String password) {
        logger.info("Performing login with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Check or uncheck "Remember me" checkbox.
     *
     * @param check true to check, false to uncheck
     * @return this LoginPage instance for method chaining
     */
    public LoginPage setRememberMe(boolean check) {
        logger.info("Setting remember me to: {}", check);
        if (isDisplayed(REMEMBER_ME_CHECKBOX)) {
            boolean isChecked = driver.findElement(REMEMBER_ME_CHECKBOX).isSelected();
            if (isChecked != check) {
                click(REMEMBER_ME_CHECKBOX);
            }
        }
        return this;
    }

    /**
     * Click "Forgot Your Password?" link.
     */
    public void clickForgotPassword() {
        logger.info("Clicking forgot password link");
        click(FORGOT_PASSWORD_LINK);
    }

    /**
     * Get error message text.
     *
     * @return error message text, empty string if no error
     */
    public String getErrorMessage() {
        logger.info("Getting error message");
        if (isDisplayed(ERROR_MESSAGE)) {
            return getText(ERROR_MESSAGE);
        }
        return "";
    }

    /**
     * Check if error message is displayed.
     *
     * @return true if error message is visible
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    /**
     * Get page title.
     *
     * @return page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}

