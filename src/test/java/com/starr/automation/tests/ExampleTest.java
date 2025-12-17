package com.starr.automation.tests;

import com.starr.automation.base.BaseTest;
import com.starr.automation.pages.HomePage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test class demonstrating test structure and best practices.
 */
@Epic("Web Application")
@Feature("Home Page")
@DisplayName("Home Page Tests")
class ExampleTest extends BaseTest {

    private HomePage homePage;

    @BeforeEach
    void setUpTest() {
        homePage = new HomePage(driver);
    }

    @Test
    @Story("Page Navigation")
    @DisplayName("Verify home page loads successfully")
    @Description("Test to verify that the home page loads and displays correctly")
    @Severity(SeverityLevel.CRITICAL)
    void testHomePageLoads() {
        // Arrange
        logger.info("Verifying home page loads");

        // Act
        boolean isLoaded = homePage.isLoaded();
        String pageTitle = homePage.getPageTitle();

        // Assert
        assertThat(isLoaded).isTrue();
        assertThat(pageTitle).isNotEmpty();
        logger.info("Home page loaded successfully with title: {}", pageTitle);
    }

    @Test
    @Story("Page Navigation")
    @DisplayName("Verify page URL is correct")
    @Description("Test to verify that the page URL matches expected URL")
    @Severity(SeverityLevel.NORMAL)
    void testPageUrl() {
        // Arrange
        logger.info("Verifying page URL");

        // Act
        String currentUrl = homePage.getCurrentUrl();

        // Assert
        assertThat(currentUrl).isNotEmpty();
        logger.info("Current URL: {}", currentUrl);
    }

    @Test
    @Story("Example Test")
    @DisplayName("Example parameterized test placeholder")
    @Description("Placeholder for demonstrating parameterized tests")
    @Disabled("Example test - implement with actual test logic")
    void exampleParameterizedTest() {
        // This is a placeholder for demonstrating test structure
        logger.info("Example parameterized test");
    }
}

