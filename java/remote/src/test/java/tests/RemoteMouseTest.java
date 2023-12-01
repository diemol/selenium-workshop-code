package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

class RemoteMouseTest {

    WebDriver driver;

    @BeforeEach
    void setup() throws MalformedURLException {
        URL gridUrl = new URL("http://localhost:4444/");
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(gridUrl, options);
    }

    @AfterEach
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void testNavigation() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        driver.findElement(By.xpath("//a[text()='Navigation']")).click();
        driver.findElement(By.xpath("//a[text()='Next']")).click();
        driver.findElement(By.xpath("//a[text()='3']")).click();
        driver.findElement(By.xpath("//a[text()='2']")).click();
        driver.findElement(By.xpath("//a[text()='Previous']")).click();

        String bodyText = driver.findElement(By.tagName("body")).getText();

        assertTrue(bodyText.contains("Lorem ipsum"), "Body did not contain 'Lorem ipsum'");
        
    }

    @Test
    public void clickAndHold() {
        driver.get("https://www.selenium.dev/selenium/web/mouse_interaction.html");

        WebElement clickable = driver.findElement(By.id("clickable"));
        new Actions(driver)
                .clickAndHold(clickable)
                .perform();

        assertEquals("focused", driver.findElement(By.id("click-status")).getText());
    }

    @Test
    public void clickAndHoldToDraw() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/draw-in-canvas.html");
        Actions actions = new Actions(driver);

        WebElement canvas = driver.findElement(By.tagName("canvas"));
        actions.moveToElement(canvas).clickAndHold();

        int numPoints = 10;
        int radius = 30;
        for (int i = 0; i <= numPoints; i++) {
            double angle = Math.toRadians(360 * i / numPoints);
            double x = Math.sin(angle) * radius;
            double y = Math.cos(angle) * radius;
            actions.moveByOffset((int) x, (int) y);
        }

        actions.release(canvas).build().perform();
    }    

    @Test
    public void clickAndRelease() {
        driver.get("https://www.selenium.dev/selenium/web/mouse_interaction.html");

        WebElement clickable = driver.findElement(By.id("click"));
        new Actions(driver)
                .click(clickable)
                .perform();

        assertTrue(driver.getCurrentUrl().contains("resultPage.html"));
    }

    @Test
    public void rightClick() {
        driver.get("https://www.selenium.dev/selenium/web/mouse_interaction.html");

        WebElement clickable = driver.findElement(By.id("clickable"));
        new Actions(driver)
                .contextClick(clickable)
                .perform();

        assertEquals("context-clicked", driver.findElement(By.id("click-status")).getText());
    }

    @Test
    public void doubleClick() {
        driver.get("https://www.selenium.dev/selenium/web/mouse_interaction.html");

        WebElement clickable = driver.findElement(By.id("clickable"));
        new Actions(driver)
                .doubleClick(clickable)
                .perform();

        assertEquals("double-clicked", driver.findElement(By.id("click-status")).getText());
    }

    @Test
    public void hovers() {
        driver.get("https://www.selenium.dev/selenium/web/mouse_interaction.html");

        WebElement hoverable = driver.findElement(By.id("hover"));
        new Actions(driver)
                .moveToElement(hoverable)
                .perform();

        assertEquals("hovered", driver.findElement(By.id("move-status")).getText());
    }

    @Test
    public void dragsToElement() {
        driver.get("https://www.selenium.dev/selenium/web/mouse_interaction.html");

        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("droppable"));
        new Actions(driver)
                .dragAndDrop(draggable, droppable)
                .perform();

        assertEquals("dropped", driver.findElement(By.id("drop-status")).getText());
    }

}
