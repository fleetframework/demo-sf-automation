package com.starr.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Page Object Model for Salesforce Home Page.
 * Common locators for Salesforce Lightning Experience.
 * Note: These are standard Salesforce locators - verify and adjust based on your specific org.
 */
public class HomePage extends BasePage {

    // Navigation and Header locators
    private static final By APP_LAUNCHER = By.xpath("//div[@role='navigation']//button[@title='App Launcher']");
    private static final By GLOBAL_SEARCH = By.cssSelector("input[placeholder*='Search']");
    private static final By USER_AVATAR = By.cssSelector(".profile-card-avatar");
    private static final By HELP_MENU = By.cssSelector("button[title='Help']");
    private static final By SETUP_LINK = By.cssSelector("a[title='Setup']");

    // Navigation bar
    private static final By NAV_BAR = By.cssSelector("nav[role='navigation']");
    private static final By NAV_ITEMS = By.cssSelector("one-app-nav-bar-item-root");

    // Main content area
    private static final By PAGE_TITLE = By.cssSelector(".slds-page-header__title");
    private static final By NEW_BUTTON = By.cssSelector("a[title='New']");
    private static final By LIST_VIEW_SELECTOR = By.cssSelector("button[title*='List View']");

    // Recent items
    private static final By RECENT_ITEMS_LIST = By.cssSelector(".forceRecordLayout");

    // Notifications
    private static final By NOTIFICATION_BELL = By.cssSelector("button[title='Notifications']");
    private static final By NOTIFICATION_BADGE = By.cssSelector(".notification-badge");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to home page.
     *
     * @param url home page URL
     */
    public void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
    }

    /**
     * Verify home page is loaded by checking for key elements.
     *
     * @return true if home page is loaded
     */
    public boolean isLoaded() {
        logger.info("Verifying home page is loaded");
        try {
            return isDisplayed(APP_LAUNCHER) || isDisplayed(NAV_BAR);
        } catch (Exception e) {
            logger.error("Error verifying home page loaded", e);
            return false;
        }
    }

    /**
     * Click the App Launcher (waffle icon).
     */
    public void clickAppLauncher() {
        logger.info("Clicking App Launcher");
        click(APP_LAUNCHER);
    }

    /**
     * Search using global search.
     *
     * @param searchTerm term to search for
     */
    public void globalSearch(String searchTerm) {
        logger.info("Performing global search for: {}", searchTerm);
        type(GLOBAL_SEARCH, searchTerm);
        driver.findElement(GLOBAL_SEARCH).submit();
    }

    /**
     * Click on user avatar to open user menu.
     */
    public void clickUserAvatar() {
        logger.info("Clicking user avatar");
        click(USER_AVATAR);
    }

    /**
     * Navigate to Setup.
     */
    public void navigateToSetup() {
        logger.info("Navigating to Setup");
        clickUserAvatar();
        click(SETUP_LINK);
    }

    /**
     * Click Help menu.
     */
    public void clickHelp() {
        logger.info("Clicking Help menu");
        click(HELP_MENU);
    }

    /**
     * Get page title.
     *
     * @return page title text
     */
    public String getPageTitle() {
        logger.info("Getting page title");
        if (isDisplayed(PAGE_TITLE)) {
            return getText(PAGE_TITLE);
        }
        return driver.getTitle();
    }

    /**
     * Click navigation item by visible text.
     *
     * @param itemName name of navigation item
     */
    public void clickNavigationItem(String itemName) {
        logger.info("Clicking navigation item: {}", itemName);
        By navItem = By.xpath("//one-app-nav-bar-item-root//span[text()='" + itemName + "']");
        click(navItem);
    }

    /**
     * Check if notification badge is displayed.
     *
     * @return true if notification badge is visible
     */
    public boolean hasNotifications() {
        logger.info("Checking for notifications");
        return isDisplayed(NOTIFICATION_BADGE);
    }

    /**
     * Click notifications bell.
     */
    public void clickNotifications() {
        logger.info("Clicking notifications");
        click(NOTIFICATION_BELL);
    }

    /**
     * Click New button.
     */
    public void clickNewButton() {
        logger.info("Clicking New button");
        click(NEW_BUTTON);
    }

    /**
     * Get all navigation items text.
     *
     * @return list of navigation item names
     */
    public List<String> getNavigationItems() {
        logger.info("Getting all navigation items");
        List<WebElement> items = driver.findElements(NAV_ITEMS);
        return items.stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty())
                .toList();
    }
}

