package com.starr.automation.listeners;

import com.starr.automation.driver.DriverFactory;
import com.starr.automation.utils.ScreenshotUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Test listener for capturing screenshots on test failure.
 */
public class TestListener implements TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.error("Test failed: {}", context.getDisplayName(), cause);

        try {
            if (DriverFactory.getDriver() != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                        DriverFactory.getDriver(),
                        context.getDisplayName()
                );
                logger.info("Screenshot captured: {}", screenshotPath);

                // Capture for Allure report
                ScreenshotUtil.captureScreenshotAsBytes(DriverFactory.getDriver());
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure", e);
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("Test passed: {}", context.getDisplayName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.warn("Test aborted: {}", context.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logger.info("Test disabled: {} - Reason: {}",
                context.getDisplayName(),
                reason.orElse("No reason provided"));
    }
}

