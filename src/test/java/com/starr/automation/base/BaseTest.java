package com.starr.automation.base;

import com.starr.automation.config.ConfigReader;
import com.starr.automation.driver.DriverFactory;
import com.starr.automation.utils.ScreenshotUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}

