package tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

class RemoteTest {

    WebDriver driver;

    @BeforeEach
    void setup() throws MalformedURLException {
        URL gridUrl = new URL("http://localhost:4444/");
        FirefoxOptions options = new FirefoxOptions();
        driver = new RemoteWebDriver(gridUrl, options);
    }

    @AfterEach
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testRemote() {
        driver.get("https://selenium.dev");
    }
}