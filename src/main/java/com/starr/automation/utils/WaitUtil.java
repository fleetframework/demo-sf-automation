package com.starr.automation.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Utility class for custom waits.
 */
public class WaitUtil {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtil.class);

    private WaitUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Wait for page to be fully loaded.
     *
     * @param driver  WebDriver instance
     * @param timeout timeout in seconds
     */
    public static void waitForPageLoad(WebDriver driver, int timeout) {
        ExpectedCondition<Boolean> pageLoadCondition = d -> {
            JavascriptExecutor js = (JavascriptExecutor) d;
            return js.executeScript("return document.readyState").equals("complete");
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(pageLoadCondition);
        logger.debug("Page loaded successfully");
    }

    /**
     * Wait for jQuery to be loaded.
     *
     * @param driver  WebDriver instance
     * @param timeout timeout in seconds
     */
    public static void waitForJQuery(WebDriver driver, int timeout) {
        ExpectedCondition<Boolean> jQueryLoad = d -> {
            try {
                JavascriptExecutor js = (JavascriptExecutor) d;
                return (Boolean) js.executeScript("return jQuery.active == 0");
            } catch (Exception e) {
                return true;
            }
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(jQueryLoad);
        logger.debug("jQuery loaded successfully");
    }

    /**
     * Wait for Angular to be loaded.
     *
     * @param driver  WebDriver instance
     * @param timeout timeout in seconds
     */
    public static void waitForAngular(WebDriver driver, int timeout) {
        ExpectedCondition<Boolean> angularLoad = d -> {
            try {
                JavascriptExecutor js = (JavascriptExecutor) d;
                return (Boolean) js.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
            } catch (Exception e) {
                return true;
            }
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(angularLoad);
        logger.debug("Angular loaded successfully");
    }
}

