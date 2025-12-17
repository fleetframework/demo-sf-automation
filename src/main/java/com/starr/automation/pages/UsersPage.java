package com.starr.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page Object Model for Salesforce Users Management Page.
 * Locators extracted using Playwright MCP analysis.
 */
public class UsersPage extends BasePage {

    // Page header locators
    private static final By PAGE_HEADING = By.xpath("//h1[text()='All Users']");
    private static final By HELP_LINK = By.linkText("Help for this Page");

    // View selection
    private static final By VIEW_DROPDOWN = By.cssSelector("select[title='View:']");
    private static final By CLONE_VIEW_LINK = By.linkText("Clone");
    private static final By CREATE_NEW_VIEW_LINK = By.linkText("Create New View");

    // User table locators
    private static final By USER_TABLE = By.cssSelector("table.list");
    private static final By USER_TABLE_ROWS = By.cssSelector("table.list tbody tr");
    private static final By TABLE_HEADER = By.cssSelector("table.list thead tr");

    // Table column headers
    private static final By FULL_NAME_HEADER = By.xpath("//th//a[contains(text(), 'Full Name')]");
    private static final By ALIAS_HEADER = By.xpath("//th//a[text()='Alias']");
    private static final By USERNAME_HEADER = By.xpath("//th//a[text()='Username']");
    private static final By ROLE_HEADER = By.xpath("//th//a[text()='Role']");
    private static final By ACTIVE_HEADER = By.xpath("//th//a[text()='Active']");
    private static final By PROFILE_HEADER = By.xpath("//th//a[text()='Profile']");

    // Alphabet navigation
    private static final By ALPHABET_NAV = By.cssSelector("div.listViewport");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public UsersPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verify Users page is loaded.
     *
     * @return true if Users page is displayed
     */
    public boolean isLoaded() {
        logger.info("Verifying Users page is loaded");
        try {
            // Wait for iframe content to load
            Thread.sleep(2000);

            // Switch to iframe if present
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            if (!iframes.isEmpty()) {
                driver.switchTo().frame(iframes.get(0));
            }

            boolean loaded = isDisplayed(PAGE_HEADING) || isDisplayed(VIEW_DROPDOWN);

            // Switch back to default content
            driver.switchTo().defaultContent();

            return loaded;
        } catch (Exception e) {
            logger.error("Error verifying Users page loaded", e);
            driver.switchTo().defaultContent();
            return false;
        }
    }

    /**
     * Switch to iframe context (Users page is in iframe).
     */
    private void switchToIframe() {
        try {
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            if (!iframes.isEmpty()) {
                driver.switchTo().frame(iframes.get(0));
                logger.debug("Switched to iframe");
            }
        } catch (Exception e) {
            logger.warn("Could not switch to iframe", e);
        }
    }

    /**
     * Switch back to default content.
     */
    private void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.debug("Switched to default content");
    }

    /**
     * Select view from dropdown.
     *
     * @param viewName name of view (e.g., "All Users", "Active Users", "Admin Users")
     */
    public void selectView(String viewName) {
        logger.info("Selecting view: {}", viewName);
        switchToIframe();
        try {
            WebElement dropdown = driver.findElement(VIEW_DROPDOWN);
            dropdown.click();
            By option = By.xpath("//select[@title='View:']//option[text()='" + viewName + "']");
            click(option);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Click Clone view link.
     */
    public void clickCloneView() {
        logger.info("Clicking Clone view");
        switchToIframe();
        try {
            click(CLONE_VIEW_LINK);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Click Create New View link.
     */
    public void clickCreateNewView() {
        logger.info("Clicking Create New View");
        switchToIframe();
        try {
            click(CREATE_NEW_VIEW_LINK);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Get all users from the table.
     *
     * @return list of user data maps
     */
    public List<Map<String, String>> getAllUsers() {
        logger.info("Getting all users from table");
        List<Map<String, String>> users = new ArrayList<>();

        switchToIframe();
        try {
            List<WebElement> rows = driver.findElements(USER_TABLE_ROWS);

            for (WebElement row : rows) {
                try {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() >= 6) {
                        Map<String, String> user = new HashMap<>();
                        user.put("fullName", cells.get(1).getText());
                        user.put("alias", cells.get(2).getText());
                        user.put("username", cells.get(3).getText());
                        user.put("role", cells.get(4).getText());
                        user.put("active", cells.get(5).getText());
                        user.put("profile", cells.get(6).getText());
                        users.add(user);
                    }
                } catch (Exception e) {
                    logger.warn("Error parsing user row", e);
                }
            }

            logger.info("Found {} users", users.size());
        } finally {
            switchToDefaultContent();
        }

        return users;
    }

    /**
     * Find user by username.
     *
     * @param username username to search for
     * @return user data map or null if not found
     */
    public Map<String, String> findUserByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        List<Map<String, String>> users = getAllUsers();

        return users.stream()
                .filter(user -> user.get("username").equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Click Edit link for a user.
     *
     * @param username username of user to edit
     */
    public void editUser(String username) {
        logger.info("Editing user: {}", username);
        switchToIframe();
        try {
            By editLink = By.xpath("//td[contains(text(), '" + username + "')]//preceding-sibling::td//a[text()='Edit']");
            click(editLink);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Click on user name to view user details.
     *
     * @param username username to click
     */
    public void clickUser(String username) {
        logger.info("Clicking user: {}", username);
        switchToIframe();
        try {
            By userLink = By.xpath("//a[contains(text(), '" + username + "')]");
            click(userLink);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Navigate using alphabet letters.
     *
     * @param letter letter to click (A-Z or "Other" or "All")
     */
    public void clickAlphabetLetter(String letter) {
        logger.info("Clicking alphabet letter: {}", letter);
        switchToIframe();
        try {
            By letterLink = By.linkText(letter);
            click(letterLink);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Sort table by column.
     *
     * @param columnName column name to sort by (e.g., "Full Name", "Username", "Role")
     */
    public void sortByColumn(String columnName) {
        logger.info("Sorting by column: {}", columnName);
        switchToIframe();
        try {
            By columnHeader = By.xpath("//th//a[contains(text(), '" + columnName + "')]");
            click(columnHeader);
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Get count of users displayed.
     *
     * @return number of users in table
     */
    public int getUserCount() {
        logger.info("Getting user count");
        switchToIframe();
        try {
            List<WebElement> rows = driver.findElements(USER_TABLE_ROWS);
            int count = rows.size();
            logger.info("User count: {}", count);
            return count;
        } finally {
            switchToDefaultContent();
        }
    }

    /**
     * Check if user exists in table.
     *
     * @param username username to check
     * @return true if user exists
     */
    public boolean userExists(String username) {
        logger.info("Checking if user exists: {}", username);
        return findUserByUsername(username) != null;
    }
}

