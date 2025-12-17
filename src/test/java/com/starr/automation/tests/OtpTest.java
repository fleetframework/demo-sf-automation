package com.starr.automation.tests;

import com.starr.automation.base.BaseTest;
import com.starr.automation.config.ConfigReader;
import com.starr.automation.pages.LoginPage;
import com.starr.automation.pages.VerificationPage;
import com.starr.automation.utils.OtpUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OTP generation and 2FA verification functionality.
 * These tests require OTP to be properly configured.
 */
public class OtpTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(OtpTest.class);

    /**
     * Check if OTP tests should run (OTP is enabled in config).
     */
    static boolean isOtpEnabled() {
        return ConfigReader.isOtpEnabled() &&
               ConfigReader.getOtpSecret() != null &&
               !ConfigReader.getOtpSecret().equals("YOUR_OTP_SECRET_HERE");
    }

    @Test
    public void testOtpGeneration() {
        logger.info("Test: OTP generation utility");

        // Test with a known secret (this is a test secret, not a real one)
        String testSecret = "JBSWY3DPEHPK3PXP";
        String otp = OtpUtil.generateOtp(testSecret);

        assertNotNull(otp, "OTP should not be null");
        assertEquals(6, otp.length(), "OTP should be 6 digits");
        assertTrue(otp.matches("\\d{6}"), "OTP should contain only digits");

        logger.info("Generated test OTP: {}", otp);
    }

    @Test
    public void testOtpValidation() {
        logger.info("Test: OTP validation");

        String testSecret = "JBSWY3DPEHPK3PXP";
        String otp = OtpUtil.generateOtp(testSecret);

        // Validate the OTP we just generated
        boolean isValid = OtpUtil.validateOtp(testSecret, otp);
        assertTrue(isValid, "OTP should be valid");

        // Test invalid OTP
        boolean isInvalid = OtpUtil.validateOtp(testSecret, "000000");
        assertFalse(isInvalid, "Wrong OTP should be invalid");
    }

    @Test
    public void testOtpRemainingTime() {
        logger.info("Test: OTP remaining time");

        int remainingSeconds = OtpUtil.getRemainingSeconds();

        assertTrue(remainingSeconds > 0, "Remaining seconds should be positive");
        assertTrue(remainingSeconds <= 30, "Remaining seconds should not exceed time step");

        logger.info("Current OTP expires in {} seconds", remainingSeconds);
    }

    @Test
    public void testOtpTimeStep() {
        logger.info("Test: OTP time step");

        int timeStep = OtpUtil.getTimeStep();
        assertEquals(30, timeStep, "Time step should be 30 seconds (standard)");
    }

    @Test
    @EnabledIf("isOtpEnabled")
    public void testLoginWithAutoOtp() {
        logger.info("Test: Login with auto-generated OTP");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        String username = ConfigReader.getTestUserEmail();
        String password = ConfigReader.getTestUserPassword();

        loginPage.login(username, password);

        // Wait for verification page to load
        try {
            Thread.sleep(3000);

            VerificationPage verificationPage = new VerificationPage(driver);
            if (verificationPage.isLoaded()) {
                logger.info("Verification page loaded, generating OTP");

                // Generate and verify with OTP
                verificationPage.verifyWithAutoOtp(true);

                // Wait for navigation
                Thread.sleep(3000);

                // Should be redirected away from verification page
                String currentUrl = driver.getCurrentUrl();
                assertFalse(currentUrl.contains("verification"),
                        "Should be redirected away from verification page after successful OTP");

                logger.info("Successfully verified with auto-generated OTP");
            } else {
                logger.info("Verification page not loaded, 2FA may not be required");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    @EnabledIf("isOtpEnabled")
    public void testManualOtpGeneration() {
        logger.info("Test: Manual OTP generation from config");

        VerificationPage verificationPage = new VerificationPage(driver);

        // Generate OTP without navigating (just testing the generation)
        String otp = verificationPage.generateOtp();

        assertNotNull(otp, "OTP should be generated");
        assertEquals(6, otp.length(), "OTP should be 6 digits");
        assertTrue(otp.matches("\\d{6}"), "OTP should contain only digits");

        logger.info("Successfully generated OTP from config: {}", otp);
    }

    @Test
    @EnabledIf("isOtpEnabled")
    public void testOtpWithFreshCodeWait() {
        logger.info("Test: OTP generation with fresh code wait");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(ConfigReader.getBaseUrl());

        String username = ConfigReader.getTestUserEmail();
        String password = ConfigReader.getTestUserPassword();

        loginPage.login(username, password);

        try {
            Thread.sleep(3000);

            VerificationPage verificationPage = new VerificationPage(driver);
            if (verificationPage.isLoaded()) {
                logger.info("Verification page loaded");

                int remainingTime = OtpUtil.getRemainingSeconds();
                logger.info("Current OTP expires in {} seconds", remainingTime);

                // This will wait for a fresh code if current one is about to expire
                verificationPage.verifyWithAutoOtp(true, true);

                // Wait for navigation
                Thread.sleep(3000);

                String currentUrl = driver.getCurrentUrl();
                assertFalse(currentUrl.contains("verification"),
                        "Should be redirected after successful verification");

                logger.info("Successfully verified with fresh OTP code");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    public void testOtpWithInvalidSecret() {
        logger.info("Test: OTP generation with invalid secret");

        // Test with invalid secret format
        assertThrows(Exception.class, () -> {
            OtpUtil.generateOtp("INVALID@#$SECRET");
        }, "Should throw exception for invalid secret format");
    }

    @Test
    public void testMultipleOtpGenerations() {
        logger.info("Test: Multiple OTP generations");

        String testSecret = "JBSWY3DPEHPK3PXP";

        // Generate multiple OTPs in quick succession
        String otp1 = OtpUtil.generateOtp(testSecret);
        String otp2 = OtpUtil.generateOtp(testSecret);
        String otp3 = OtpUtil.generateOtp(testSecret);

        // All should be the same (same time window)
        assertEquals(otp1, otp2, "OTPs generated in same time window should match");
        assertEquals(otp2, otp3, "OTPs generated in same time window should match");

        logger.info("Multiple generations produced consistent OTP: {}", otp1);
    }

    @Test
    public void testOtpChangesOverTime() throws InterruptedException {
        logger.info("Test: OTP changes over time");

        String testSecret = "JBSWY3DPEHPK3PXP";
        String otp1 = OtpUtil.generateOtp(testSecret);

        int remainingSeconds = OtpUtil.getRemainingSeconds();
        logger.info("Waiting {} seconds for OTP to change", remainingSeconds + 1);

        // Wait for the time window to change
        Thread.sleep((remainingSeconds + 1) * 1000L);

        String otp2 = OtpUtil.generateOtp(testSecret);

        assertNotEquals(otp1, otp2, "OTP should change after time window expires");
        logger.info("OTP changed from {} to {} after time window", otp1, otp2);
    }
}

