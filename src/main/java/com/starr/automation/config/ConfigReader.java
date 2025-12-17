package com.starr.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration reader for loading and accessing application properties.
 * Loads both config.properties (non-sensitive) and secrets.properties (sensitive credentials).
 */
public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE = "config.properties";
    private static final String SECRETS_FILE = "secrets.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    private ConfigReader() {
        // Private constructor to prevent instantiation
    }

    /**
     * Load properties from config files.
     * Loads config.properties first, then secrets.properties (which can override values).
     */
    private static void loadProperties() {
        properties = new Properties();

        // Load main config file
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Unable to find {}", CONFIG_FILE);
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Error loading configuration from {}", CONFIG_FILE, e);
        }

        // Load secrets file (optional)
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(SECRETS_FILE)) {
            if (input != null) {
                properties.load(input);
                logger.info("Secrets loaded successfully from {}", SECRETS_FILE);
            } else {
                logger.warn("Secrets file {} not found - using values from {} or environment variables",
                        SECRETS_FILE, CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.warn("Error loading secrets from {}", SECRETS_FILE, e);
        }
    }

    /**
     * Get property value by key.
     *
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value with default fallback.
     *
     * @param key          the property key
     * @param defaultValue the default value if key not found
     * @return the property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get browser type from configuration.
     *
     * @return browser type
     */
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    /**
     * Get base URL from configuration.
     *
     * @return base URL
     */
    public static String getBaseUrl() {
        return getProperty("base.url", "https://www.example.com");
    }

    /**
     * Get implicit wait timeout.
     *
     * @return timeout in seconds
     */
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    /**
     * Get explicit wait timeout.
     *
     * @return timeout in seconds
     */
    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "20"));
    }

    /**
     * Get page load timeout.
     *
     * @return timeout in seconds
     */
    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "30"));
    }

    /**
     * Check if headless mode is enabled.
     *
     * @return true if headless mode is enabled
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    /**
     * Get OTP secret key for 2FA.
     *
     * @return OTP secret in Base32 format
     */
    public static String getOtpSecret() {
        return getProperty("test.otp.secret");
    }

    /**
     * Check if OTP generation is enabled.
     *
     * @return true if OTP is enabled
     */
    public static boolean isOtpEnabled() {
        return Boolean.parseBoolean(getProperty("test.otp.enabled", "false"));
    }

    /**
     * Get test user email.
     *
     * @return test user email
     */
    public static String getTestUserEmail() {
        return getProperty("test.user.email");
    }

    /**
     * Get test user password.
     *
     * @return test user password
     */
    public static String getTestUserPassword() {
        return getProperty("test.user.password");
    }
}

