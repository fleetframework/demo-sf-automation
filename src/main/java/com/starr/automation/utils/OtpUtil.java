package com.starr.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

/**
 * Utility class for generating Time-based One-Time Passwords (TOTP).
 * Implements RFC 6238 TOTP algorithm compatible with Google Authenticator.
 */
public class OtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(OtpUtil.class);

    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
    private static final int TIME_STEP = 30; // 30 seconds time step (standard)
    private static final int CODE_DIGITS = 6; // 6 digit code (standard)
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    /**
     * Generate TOTP code from secret key.
     *
     * @param secretKey the secret key in Base32 format (from authenticator app setup)
     * @return 6-digit OTP code
     */
    public static String generateOtp(String secretKey) {
        try {
            long timeCounter = Instant.now().getEpochSecond() / TIME_STEP;
            byte[] decodedKey = decodeBase32(secretKey);
            return generateTOTP(decodedKey, timeCounter, CODE_DIGITS);
        } catch (Exception e) {
            logger.error("Error generating OTP", e);
            throw new RuntimeException("Failed to generate OTP", e);
        }
    }

    /**
     * Generate TOTP code with custom parameters.
     *
     * @param secretKey the secret key in Base32 format
     * @param timeStep time step in seconds
     * @param digits number of digits in the code
     * @return OTP code
     */
    public static String generateOtp(String secretKey, int timeStep, int digits) {
        try {
            long timeCounter = Instant.now().getEpochSecond() / timeStep;
            byte[] decodedKey = decodeBase32(secretKey);
            return generateTOTP(decodedKey, timeCounter, digits);
        } catch (Exception e) {
            logger.error("Error generating OTP with custom parameters", e);
            throw new RuntimeException("Failed to generate OTP", e);
        }
    }

    /**
     * Generate TOTP for a specific time (useful for testing).
     *
     * @param secretKey the secret key in Base32 format
     * @param epochSeconds specific time in epoch seconds
     * @return OTP code for that specific time
     */
    public static String generateOtpForTime(String secretKey, long epochSeconds) {
        try {
            long timeCounter = epochSeconds / TIME_STEP;
            byte[] decodedKey = decodeBase32(secretKey);
            return generateTOTP(decodedKey, timeCounter, CODE_DIGITS);
        } catch (Exception e) {
            logger.error("Error generating OTP for specific time", e);
            throw new RuntimeException("Failed to generate OTP", e);
        }
    }

    /**
     * Core TOTP generation algorithm (RFC 6238).
     *
     * @param key secret key bytes
     * @param timeCounter time counter value
     * @param digits number of digits in the code
     * @return TOTP code
     */
    private static String generateTOTP(byte[] key, long timeCounter, int digits)
            throws NoSuchAlgorithmException, InvalidKeyException {

        // Convert counter to byte array
        byte[] data = ByteBuffer.allocate(8).putLong(timeCounter).array();

        // Generate HMAC-SHA1 hash
        SecretKeySpec signKey = new SecretKeySpec(key, HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        // Dynamic truncation (RFC 4226)
        int offset = hash[hash.length - 1] & 0x0F;
        int binary = ((hash[offset] & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                | (hash[offset + 3] & 0xFF);

        int otp = binary % DIGITS_POWER[digits];

        // Pad with leading zeros
        String result = String.valueOf(otp);
        while (result.length() < digits) {
            result = "0" + result;
        }

        logger.debug("Generated OTP: {}", result);
        return result;
    }

    /**
     * Decode Base32 encoded secret key.
     *
     * @param base32 Base32 encoded string
     * @return decoded byte array
     */
    private static byte[] decodeBase32(String base32) {
        // Remove spaces and convert to uppercase
        base32 = base32.replace(" ", "").toUpperCase();

        // Base32 alphabet (RFC 4648)
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

        // Calculate output length
        int outputLength = base32.length() * 5 / 8;
        byte[] result = new byte[outputLength];

        int buffer = 0;
        int bitsLeft = 0;
        int index = 0;

        for (char c : base32.toCharArray()) {
            int value = alphabet.indexOf(c);
            if (value < 0) {
                continue; // Skip invalid characters
            }

            buffer <<= 5;
            buffer |= value;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                result[index++] = (byte) (buffer >> (bitsLeft - 8));
                bitsLeft -= 8;
            }
        }

        return result;
    }

    /**
     * Validate if OTP code is correct for the given secret.
     * Useful for testing.
     *
     * @param secretKey the secret key in Base32 format
     * @param otp the OTP code to validate
     * @return true if OTP is valid
     */
    public static boolean validateOtp(String secretKey, String otp) {
        try {
            String generatedOtp = generateOtp(secretKey);
            return generatedOtp.equals(otp);
        } catch (Exception e) {
            logger.error("Error validating OTP", e);
            return false;
        }
    }

    /**
     * Get remaining time in seconds until current OTP expires.
     *
     * @return seconds until next OTP
     */
    public static int getRemainingSeconds() {
        long currentTime = Instant.now().getEpochSecond();
        return TIME_STEP - (int) (currentTime % TIME_STEP);
    }

    /**
     * Get the time step used for OTP generation.
     *
     * @return time step in seconds
     */
    public static int getTimeStep() {
        return TIME_STEP;
    }
}

