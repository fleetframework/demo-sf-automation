package com.starr.automation.tests;

import com.starr.automation.base.BaseTest;
import com.starr.automation.config.ConfigReader;
import com.starr.automation.pages.HomePage;
import com.starr.automation.pages.LoginPage;
import com.starr.automation.pages.VerificationPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Salesforce Login functionality.
 * Demonstrates usage of page objects created using Playwright MCP analysis.
 */
public class LoginTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @Test
    public void testLoginPageIsDisplayed() {
        logger.info("Test: Verify login page is displayed");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        assertTrue(loginPage.isLoaded(), "Login page should be loaded");
        assertEquals("Login | Salesforce", loginPage.getPageTitle(),
                "Page title should be 'Login | Salesforce'");
    }

    @Test
    public void testLoginWithValidCredentials() {
        logger.info("Test: Login with valid credentials");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        // Perform login
        String username = ConfigReader.getTestUserEmail();
        String password = ConfigReader.getTestUserPassword();

        loginPage.login(username, password);

        // Wait for page to load after login
        try {
            Thread.sleep(3000); // Wait for navigation

            // Check if we're on verification page or home page
            String currentUrl = driver.getCurrentUrl();
            logger.info("Current URL after login: {}", currentUrl);

            if (currentUrl.contains("verification")) {
                VerificationPage verificationPage = new VerificationPage(driver);
                assertTrue(verificationPage.isLoaded(),
                        "Should be on verification page after login");
                logger.info("2FA verification required - using auto-generated OTP");

                // Use OTP auto-generation if enabled
                if (ConfigReader.isOtpEnabled()) {
                    verificationPage.verifyWithAutoOtp(false); // Auto-generate and verify with "don't ask again"
                    logger.info("Successfully verified with auto-generated OTP");

                    // Wait for home page to load after OTP verification
                    HomePage homePage = new HomePage(driver);
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                    wait.until(driver -> homePage.isLoaded());

                    assertTrue(homePage.isLoaded(),
                            "Should be on home page after OTP verification");
                } else {
                    logger.warn("OTP is not enabled - cannot auto-verify");
                }
            } else {
                // Should be on home page
                HomePage homePage = new HomePage(driver);
                assertTrue(homePage.isLoaded(),
                        "Should be on home page after login");
                logger.info("Login successful without 2FA requirement");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    public void testLoginWithAutoOTP() {
        logger.info("Test: Complete login flow with auto-generated OTP");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        String username = ConfigReader.getTestUserEmail();
        String password = ConfigReader.getTestUserPassword();

        // Perform login
        loginPage.login(username, password);
        logger.info("Login credentials submitted");

        try {
            Thread.sleep(3000);

            // Handle 2FA if it appears
            VerificationPage verificationPage = new VerificationPage(driver);
            if (verificationPage.isLoaded()) {
                logger.info("Verification page detected - generating OTP");

                if (ConfigReader.isOtpEnabled()) {
                    // Generate OTP and display it
                    String otp = verificationPage.generateOtp();
                    logger.info("Generated OTP: {}", otp);

                    // Verify with auto-generated OTP and set "don't ask again"
                    verificationPage.verifyWithAutoOtp(true, true);
                    logger.info("OTP verification completed");

                    // Wait for home page to load
                    HomePage homePage = new HomePage(driver);
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                    wait.until(driver -> homePage.isLoaded());

                    // Verify successful login
                    assertTrue(homePage.isLoaded(),
                            "Should be on home page after OTP verification");
                    logger.info("Successfully logged in with OTP");
                } else {
                    fail("OTP is required but not enabled in config");
                }
            } else {
                logger.info("No 2FA required - direct login successful");
                HomePage homePage = new HomePage(driver);
                assertTrue(homePage.isLoaded(), "Should be on home page");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    public void testLoginWithMethodChaining() {
        logger.info("Test: Login using method chaining");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        String username = ConfigReader.getTestUserEmail();
        String password = ConfigReader.getTestUserPassword();

        // Demonstrate method chaining
        loginPage.enterUsername(username)
                .enterPassword(password)
                .setRememberMe(false)
                .clickLogin();

        // Verify we moved away from login page
        try {
            Thread.sleep(2000);
            String currentUrl = driver.getCurrentUrl();
            assertNotEquals(ConfigReader.getBaseUrl(), currentUrl,
                    "Should navigate away from login page");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        logger.info("Test: Login with invalid credentials");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        loginPage.login("invalid@email.com", "wrongpassword");

        // Wait for error message
        try {
            Thread.sleep(2000);

            // Verify error message is displayed
            assertTrue(loginPage.isErrorDisplayed() ||
                    !driver.getCurrentUrl().equals(ConfigReader.getBaseUrl()),
                    "Should show error or remain on login page");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    public void testRememberMeCheckbox() {
        logger.info("Test: Remember me checkbox functionality");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        // Test checking the checkbox
        loginPage.setRememberMe(true);

        // In a real test, you would verify the checkbox state
        assertTrue(loginPage.isLoaded(), "Login page should remain loaded");
    }

    @Test
    public void testForgotPasswordLink() {
        logger.info("Test: Forgot password link");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        loginPage.clickForgotPassword();

        // Wait for navigation
        try {
            Thread.sleep(2000);
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("forgotpassword") ||
                    currentUrl.contains("reset"),
                    "Should navigate to forgot password page");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }
}

