package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class TimeoutsTest {

    WebDriver driver;

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testScriptTimeout() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3));

        assertThrows(
            ScriptTimeoutException.class,
            () -> {
                long waitMillis = Duration.ofSeconds(5).toMillis();
                String script = "const callback = arguments[arguments.length - 1];"
                        + "window.setTimeout(callback, " + waitMillis + ");";
                js.executeAsyncScript(script);                
            },
            "Expected script to throw ScriptTimeoutException, but it didn't"
        );
    }

    @Test
    void testPageLoadTimeout() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(1));
        assertThrows(
            TimeoutException.class,
            () -> {
                driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
            },
            "Expected script to throw TimeoutException, but it didn't"
        );
    }

}
