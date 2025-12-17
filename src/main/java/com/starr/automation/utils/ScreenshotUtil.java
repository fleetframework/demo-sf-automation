package com.starr.automation.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for capturing screenshots.
 */
public class ScreenshotUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";

    private ScreenshotUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Capture screenshot and save to file.
     *
     * @param driver   WebDriver instance
     * @param fileName screenshot file name
     * @return path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String fileName) {
        try {
            // Create screenshots directory if it doesn't exist
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
            }

            // Generate timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotName = fileName + "_" + timestamp + ".png";

            // Capture screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(SCREENSHOT_DIR + File.separator + screenshotName);

            // Copy file
            Files.copy(sourceFile.toPath(), destFile.toPath());
            logger.info("Screenshot saved: {}", destFile.getAbsolutePath());

            return destFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Capture screenshot as byte array for Allure report.
     *
     * @param driver WebDriver instance
     * @return screenshot as byte array
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}

