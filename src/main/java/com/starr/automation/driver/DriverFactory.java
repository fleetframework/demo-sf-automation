package com.starr.automation.driver;

import com.starr.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Factory class for creating and managing WebDriver instances.
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get the WebDriver instance for the current thread.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            createDriver();
        }
        return driver.get();
    }

    /**
     * Create a new WebDriver instance based on configuration.
     */
    private static void createDriver() {
        String browser = System.getProperty("browser", ConfigReader.getBrowser());
        logger.info("Creating {} driver", browser);

        WebDriver webDriver = switch (browser.toLowerCase()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> {
                logger.warn("Unknown browser: {}. Defaulting to Chrome", browser);
                yield createChromeDriver();
            }
        };

        configureDriver(webDriver);
        driver.set(webDriver);
    }

    /**
     * Create Chrome WebDriver.
     *
     * @return Chrome WebDriver instance
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setAcceptInsecureCerts(true);

        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver.
     *
     * @return Firefox WebDriver instance
     */
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }

        options.setAcceptInsecureCerts(true);

        return new FirefoxDriver(options);
    }

    /**
     * Create Edge WebDriver.
     *
     * @return Edge WebDriver instance
     */
    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }

        options.addArguments("--start-maximized");
        options.setAcceptInsecureCerts(true);

        return new EdgeDriver(options);
    }

    /**
     * Configure WebDriver timeouts and settings.
     *
     * @param webDriver WebDriver instance to configure
     */
    private static void configureDriver(WebDriver webDriver) {
        webDriver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        webDriver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        webDriver.manage().window().maximize();
    }

    /**
     * Quit the WebDriver instance for the current thread.
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("Quitting WebDriver");
            driver.get().quit();
            driver.remove();
        }
    }
}

