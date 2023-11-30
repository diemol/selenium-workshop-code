package tests;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ScreenshotsTest {
    WebDriver driver;
    static final Logger log = Logger.getLogger(ScreenshotsTest.class.getName());

    @BeforeEach
    void setup() throws InterruptedException {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        driver.quit();
    }

    @Test
    void testScreenshotPng() throws IOException {
        driver.get("https://selenium.dev/");
        TakesScreenshot ts = (TakesScreenshot) driver;

        File screenshot = ts.getScreenshotAs(OutputType.FILE);

        Path destination = Paths.get("screenshot.png");
        Files.move(screenshot.toPath(), destination, REPLACE_EXISTING);
        log.log(Level.INFO, "Screenshot moved to {}", destination);
    }    

    @Test
    void testWebElementScreenshot() throws IOException {
        driver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");

        WebElement form = driver.findElement(By.id("berlin"));
        File screenshot = form.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get("webelement-screenshot.png");
        Files.move(screenshot.toPath(), destination, REPLACE_EXISTING);
    }    

}