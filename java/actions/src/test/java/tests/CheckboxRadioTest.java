package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

class CheckboxRadioTest {

    WebDriver driver;

    @BeforeEach
    void setup() {
        driver = new FirefoxDriver();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testCheckboxAndRadio() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        WebElement checkbox2 = driver.findElement(By.id("my-check-2"));
        checkbox2.click();
        assertTrue(checkbox2.isSelected());

        WebElement radio2 = driver.findElement(By.id("my-radio-2"));
        radio2.click();
        assertTrue(radio2.isSelected());
    }

}