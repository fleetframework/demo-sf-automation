package com.starr.automation.pages;

import com.starr.automation.config.ConfigReader;
import com.starr.automation.utils.OtpUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model for Salesforce Identity Verification Page (2FA).
 * Locators extracted using Playwright MCP analysis.
 * Supports automatic OTP generation using TOTP algorithm.
 */
public class VerificationPage extends BasePage {

    // Locators - extracted from Playwright analysis
    private static final By VERIFICATION_CODE_INPUT = By.id("tc");
    private static final By VERIFY_BUTTON = By.id("save");
    private static final By DONT_ASK_AGAIN_CHECKBOX = By.id("daa");
    private static final By RESEND_CODE_LINK = By.linkText("Resend Code");
    private static final By PAGE_HEADING = By.xpath("//h2[contains(text(), 'Verify Your Identity')]");
    private static final By ERROR_MESSAGE = By.cssSelector(".errorMsg");
    private static final By VERIFICATION_MESSAGE = By.cssSelector(".instruction");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public VerificationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verify that verification page is loaded.
     *
     * @return true if verification page is displayed
     */
    public boolean isLoaded() {
        logger.info("Verifying identity verification page is loaded");
        return isDisplayed(PAGE_HEADING) && isDisplayed(VERIFICATION_CODE_INPUT);
    }

    /**
     * Enter verification code.
     *
     * @param code verification code
     * @return this VerificationPage instance for method chaining
     */
    public VerificationPage enterVerificationCode(String code) {
        logger.info("Entering verification code");
        type(VERIFICATION_CODE_INPUT, code);
        return this;
    }

    /**
     * Click verify button.
     */
    public void clickVerify() {
        logger.info("Clicking verify button");
        click(VERIFY_BUTTON);
    }

    /**
     * Perform complete verification.
     *
     * @param code verification code
     */
    public void verify(String code) {
        logger.info("Performing verification");
        enterVerificationCode(code);
        clickVerify();
    }

    /**
     * Check or uncheck "Don't ask again" checkbox.
     *
     * @param check true to check, false to uncheck
     * @return this VerificationPage instance for method chaining
     */
    public VerificationPage setDontAskAgain(boolean check) {
        logger.info("Setting 'Don't ask again' to: {}", check);
        if (isDisplayed(DONT_ASK_AGAIN_CHECKBOX)) {
            boolean isChecked = driver.findElement(DONT_ASK_AGAIN_CHECKBOX).isSelected();
            if (isChecked != check) {
                click(DONT_ASK_AGAIN_CHECKBOX);
            }
        }
        return this;
    }

    /**
     * Click "Resend Code" link.
     */
    public void clickResendCode() {
        logger.info("Clicking resend code link");
        click(RESEND_CODE_LINK);
    }

    /**
     * Get verification instruction message.
     *
     * @return instruction message text
     */
    public String getInstructionMessage() {
        logger.info("Getting instruction message");
        if (isDisplayed(VERIFICATION_MESSAGE)) {
            return getText(VERIFICATION_MESSAGE);
        }
        return "";
    }

    /**
     * Get error message text.
     *
     * @return error message text, empty string if no error
     */
    public String getErrorMessage() {
        logger.info("Getting error message");
        if (isDisplayed(ERROR_MESSAGE)) {
            return getText(ERROR_MESSAGE);
        }
        return "";
    }

    /**
     * Check if error message is displayed.
     *
     * @return true if error message is visible
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    /**
     * Get page heading text.
     *
     * @return page heading text
     */
    public String getPageHeading() {
        if (isDisplayed(PAGE_HEADING)) {
            return getText(PAGE_HEADING);
        }
        return "";
    }

    /**
     * Generate OTP code using configured secret key.
     * Requires test.otp.secret to be configured in config.properties.
     *
     * @return generated 6-digit OTP code
     * @throws IllegalStateException if OTP secret is not configured
     */
    public String generateOtp() {
        String otpSecret = ConfigReader.getOtpSecret();
        if (otpSecret == null || otpSecret.isEmpty() || otpSecret.equals("YOUR_OTP_SECRET_HERE")) {
            String errorMsg = "OTP secret is not configured. Please set test.otp.secret in config.properties";
            logger.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        String otp = OtpUtil.generateOtp(otpSecret);
        logger.info("Generated OTP code (expires in {} seconds)", OtpUtil.getRemainingSeconds());
        return otp;
    }

    /**
     * Verify using auto-generated OTP from configured secret.
     * This method will automatically generate the OTP code and submit it.
     * Requires test.otp.enabled=true and test.otp.secret to be configured.
     */
    public void verifyWithAutoOtp() {
        if (!ConfigReader.isOtpEnabled()) {
            logger.warn("OTP auto-generation is disabled. Enable with test.otp.enabled=true");
            throw new IllegalStateException("OTP generation is disabled in configuration");
        }

        String otp = generateOtp();
        logger.info("Using auto-generated OTP for verification");
        verify(otp);
    }

    /**
     * Verify using auto-generated OTP with "Don't ask again" option.
     *
     * @param dontAskAgain whether to check "Don't ask again" checkbox
     */
    public void verifyWithAutoOtp(boolean dontAskAgain) {
        if (!ConfigReader.isOtpEnabled()) {
            logger.warn("OTP auto-generation is disabled. Enable with test.otp.enabled=true");
            throw new IllegalStateException("OTP generation is disabled in configuration");
        }

        String otp = generateOtp();
        logger.info("Using auto-generated OTP for verification with dontAskAgain={}", dontAskAgain);
        enterVerificationCode(otp);
        setDontAskAgain(dontAskAgain);
        clickVerify();
    }

    /**
     * Wait and verify with auto-generated OTP.
     * Useful when the OTP might be about to expire - waits for a fresh code.
     *
     * @param waitForFreshCode if true, waits if current OTP expires in less than 5 seconds
     */
    public void verifyWithAutoOtp(boolean waitForFreshCode, boolean dontAskAgain) {
        if (!ConfigReader.isOtpEnabled()) {
            logger.warn("OTP auto-generation is disabled. Enable with test.otp.enabled=true");
            throw new IllegalStateException("OTP generation is disabled in configuration");
        }

        if (waitForFreshCode && OtpUtil.getRemainingSeconds() < 5) {
            int waitTime = OtpUtil.getRemainingSeconds() + 1;
            logger.info("Current OTP about to expire. Waiting {} seconds for fresh code", waitTime);
            try {
                Thread.sleep(waitTime * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Wait interrupted", e);
            }
        }

        verifyWithAutoOtp(dontAskAgain);
    }
}

