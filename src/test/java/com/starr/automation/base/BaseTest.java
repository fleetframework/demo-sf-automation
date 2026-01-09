package com.starr.automation.base;

import com.starr.automation.config.ConfigReader;
import com.starr.automation.driver.DriverFactory;
import com.starr.automation.pages.BasePage;
import com.starr.automation.utils.ScreenshotUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * Base test class that all test classes should extend.
 * Handles WebDriver setup and teardown.
 */
public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected WebDriver driver;

    /**
     * Setup method executed before each test.
     *
     * @param testInfo JUnit test information
     */
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        logger.info("========================================");
        logger.info("Starting test: {}", testInfo.getDisplayName());
        logger.info("========================================");

        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getBaseUrl());
    }

    /**
     * Teardown method executed after each test.
     *
     * @param testInfo JUnit test information
     */
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        if (testInfo.getTestMethod().isPresent()) {
            logger.info("Test {} completed", testInfo.getDisplayName());
        }

        // Capture screenshot on test failure
        if (testInfo.getTags().contains("failed")) {
            ScreenshotUtil.captureScreenshotAsBytes(driver);
        }

        DriverFactory.quitDriver();
        logger.info("========================================");
    }

    /**
     * Get the WebDriver instance.
     *
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Wait for a page to load by polling its isLoaded() method.
     *
     * @param page page object that extends BasePage
     * @param timeoutSeconds maximum time to wait in seconds
     * @throws RuntimeException if page doesn't load within timeout
     */
    protected void waitForPageToLoad(BasePage page, int timeoutSeconds) {
        logger.info("Waiting for page to load (timeout: {}s)", timeoutSeconds);
        Instant startTime = Instant.now();
        Duration timeout = Duration.ofSeconds(timeoutSeconds);
        int attemptCount = 0;

        while (Duration.between(startTime, Instant.now()).compareTo(timeout) < 0) {
            attemptCount++;
            try {
                if (page.isLoaded()) {
                    logger.info("Page loaded successfully after {} attempts", attemptCount);
                    return;
                }
                logger.debug("Page not loaded yet (attempt {}), current URL: {}",
                    attemptCount, driver.getCurrentUrl());
            } catch (Exception e) {
                logger.debug("Exception while checking if page is loaded (attempt {}): {}",
                    attemptCount, e.getMessage());
            }

            try {
                Thread.sleep(500); // Poll every 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }

        logger.error("Page did not load within {} seconds. Final URL: {}",
            timeoutSeconds, driver.getCurrentUrl());
        throw new RuntimeException(String.format("Page did not load within %d seconds", timeoutSeconds));
    }
}

