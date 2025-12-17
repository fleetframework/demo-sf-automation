# Selenium Test Automation Framework

A modern Selenium WebDriver test automation framework using Java, Maven, and JUnit 5.

## Features

- **Selenium WebDriver 4.27.0** - Latest version with enhanced features
- **JUnit 5** - Modern testing framework with advanced features
- **WebDriverManager** - Automatic driver management
- **Page Object Model** - Clean separation of test logic and page structure
- **Allure Reports** - Beautiful test reporting
- **Parallel Execution** - Run tests concurrently
- **Logging** - SLF4J with Logback

## Project Structure

```
sf-automation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/starr/automation/
│   │   │       ├── config/         # Configuration management
│   │   │       ├── driver/         # WebDriver factory and management
│   │   │       ├── pages/          # Page Object Model classes
│   │   │       └── utils/          # Utility classes
│   │   └── resources/
│   │       ├── config.properties   # Configuration properties
│   │       └── logback.xml         # Logging configuration
│   └── test/
│       ├── java/
│       │   └── com/starr/automation/
│       │       ├── base/           # Base test classes
│       │       ├── tests/          # Test classes
│       │       └── listeners/      # Test listeners
│       └── resources/
│           └── test-data/          # Test data files
├── pom.xml                         # Maven configuration
└── README.md                       # This file
```

## Prerequisites

- Java 21 or higher
- Maven 3.9 or higher

## Setup

1. Clone the repository
2. Run `mvn clean install` to download dependencies

## Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn clean test -Dtest=ExampleTest

# Run with specific browser
mvn clean test -Dbrowser=chrome

# Generate Allure report
mvn allure:serve
```

## Configuration

Update `src/main/resources/config.properties` to configure:
- Browser type
- Base URL
- Timeouts
- Other test parameters

## Writing Tests

1. Create page objects in `src/main/java/com/starr/automation/pages/`
2. Create test classes in `src/test/java/com/starr/automation/tests/`
3. Extend `BaseTest` for automatic setup and teardown

## Best Practices

- Follow Page Object Model pattern
- Use descriptive test names
- Keep tests independent
- Use explicit waits over implicit waits
- Add meaningful assertions
- Document complex test scenarios

