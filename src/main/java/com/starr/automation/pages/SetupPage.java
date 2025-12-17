package com.starr.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model for Salesforce Setup Home Page.
 * Locators extracted using Playwright MCP analysis.
 */
public class SetupPage extends BasePage {

    // Header navigation locators
    private static final By SEARCH_SETUP = By.cssSelector("input[placeholder='Search Setup']");
    private static final By APP_LAUNCHER = By.cssSelector("button[title='App Launcher']");
    private static final By GLOBAL_ACTIONS_BUTTON = By.cssSelector("button[title='Global Actions']");
    private static final By GUIDANCE_CENTER_BUTTON = By.cssSelector("button[title='Guidance Center']");
    private static final By HELP_BUTTON = By.cssSelector("button[title='Salesforce Help']");
    private static final By SETUP_BUTTON = By.cssSelector("button[title='Setup']");
    private static final By NOTIFICATIONS_BUTTON = By.cssSelector("button[title='Notifications']");
    private static final By PROFILE_BUTTON = By.cssSelector("button[title='View profile']");

    // Setup tree navigation
    private static final By QUICK_FIND_INPUT = By.cssSelector("input[placeholder='Quick Find']");
    private static final By SETUP_HOME_LINK = By.xpath("//a[text()='Setup Home']");

    // Main content area
    private static final By PAGE_HEADING = By.cssSelector("h1");
    private static final By HOME_TAB = By.xpath("//button[@role='tab' and contains(text(), 'Home')]");
    private static final By OBJECT_MANAGER_TAB = By.xpath("//button[@role='tab' and contains(text(), 'Object Manager')]");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public SetupPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verify Setup page is loaded.
     *
     * @return true if Setup page is displayed
     */
    public boolean isLoaded() {
        logger.info("Verifying Setup page is loaded");
        try {
            return isDisplayed(SEARCH_SETUP) || isDisplayed(QUICK_FIND_INPUT);
        } catch (Exception e) {
            logger.error("Error verifying Setup page loaded", e);
            return false;
        }
    }

    /**
     * Search in Setup using Quick Find.
     *
     * @param searchTerm term to search for
     */
    public void quickFind(String searchTerm) {
        logger.info("Quick Find search for: {}", searchTerm);
        type(QUICK_FIND_INPUT, searchTerm);
    }

    /**
     * Navigate to Users management.
     */
    public void navigateToUsers() {
        logger.info("Navigating to Users");
        quickFind("Users");
        By usersLink = By.xpath("//a[text()='Users' and @href='/lightning/setup/ManageUsers/home']");
        click(usersLink);
    }

    /**
     * Navigate to section using Quick Find.
     *
     * @param sectionName name of section to navigate to
     */
    public void navigateToSection(String sectionName) {
        logger.info("Navigating to section: {}", sectionName);
        quickFind(sectionName);
        // Wait for search results and click first match
        try {
            Thread.sleep(1000); // Simple wait for search results
            By sectionLink = By.xpath("//a[contains(text(), '" + sectionName + "')]");
            click(sectionLink);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted", e);
        }
    }

    /**
     * Click Home tab.
     */
    public void clickHomeTab() {
        logger.info("Clicking Home tab");
        click(HOME_TAB);
    }

    /**
     * Click Object Manager tab.
     */
    public void clickObjectManagerTab() {
        logger.info("Clicking Object Manager tab");
        click(OBJECT_MANAGER_TAB);
    }

    /**
     * Click Setup button in header.
     */
    public void clickSetupButton() {
        logger.info("Clicking Setup button");
        click(SETUP_BUTTON);
    }

    /**
     * Click Help button.
     */
    public void clickHelp() {
        logger.info("Clicking Help button");
        click(HELP_BUTTON);
    }

    /**
     * Click Notifications button.
     */
    public void clickNotifications() {
        logger.info("Clicking Notifications");
        click(NOTIFICATIONS_BUTTON);
    }

    /**
     * Click Profile button to view profile.
     */
    public void clickProfile() {
        logger.info("Clicking Profile");
        click(PROFILE_BUTTON);
    }

    /**
     * Get page heading text.
     *
     * @return page heading text
     */
    public String getPageHeading() {
        logger.info("Getting page heading");
        if (isDisplayed(PAGE_HEADING)) {
            return getText(PAGE_HEADING);
        }
        return "";
    }
}

