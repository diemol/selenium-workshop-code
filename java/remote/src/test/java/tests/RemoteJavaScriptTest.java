package tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

class RemoteJavaScriptTest {

    WebDriver driver;

    @BeforeEach
    void setup() throws MalformedURLException {
        URL gridUrl = new URL("http://localhost:4444/");
        EdgeOptions options = new EdgeOptions();
        driver = new RemoteWebDriver(gridUrl, options);
    }

    @AfterEach
    void teardown() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        driver.quit();
    }

    @Test
    void blurElementsTest() throws InterruptedException {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		driver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");

		blur(jsExecutor, driver.findElement(By.id("boston")));
		blur(jsExecutor, driver.findElement(By.id("warsaw")));
		unblur(jsExecutor, driver.findElement(By.id("boston")));
		unblur(jsExecutor, driver.findElement(By.id("warsaw")));
    }

	public void blur(JavascriptExecutor jsExecutor, WebElement webElement) throws InterruptedException {
		jsExecutor.executeScript("arguments[0].style.filter='blur(8px)'", webElement);
		// Sleep only meant for demo purposes!
		Thread.sleep(Duration.ofSeconds(3).toMillis());
	}

	public void unblur(JavascriptExecutor jsExecutor, WebElement webElement) {
		jsExecutor.executeScript("arguments[0].style.filter='blur(0px)'", webElement);
	}    

    @Test
    void testScrollIntoView() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement lastElememt = driver.findElement(By.cssSelector("p:last-child"));
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, lastElememt);
    }
}