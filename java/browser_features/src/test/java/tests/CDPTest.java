package tests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.dom.model.Rect;
import org.openqa.selenium.devtools.v119.network.Network;
import org.openqa.selenium.devtools.v119.network.model.BlockedReason;
import org.openqa.selenium.devtools.v119.network.model.Headers;
import org.openqa.selenium.devtools.v119.network.model.ResourceType;
import org.openqa.selenium.devtools.v119.page.Page;
import org.openqa.selenium.devtools.v119.page.Page.GetLayoutMetricsResponse;
import org.openqa.selenium.devtools.v119.page.model.Viewport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CDPTest {
	private ChromeDriver webDriver;
	private DevTools devTools;

	@BeforeEach
	public void setUp() {
		webDriver = new ChromeDriver();
		devTools = webDriver.getDevTools();
		devTools.createSession();
	}

    @AfterEach
	public void tearDown() {
		webDriver.quit();
	}


	@Test
	public void blockUrls() throws InterruptedException {
		// Network enabled
		devTools.send(new Command<>("Network.enable", ImmutableMap.of()));

		// Block urls that have png and css
		devTools.send(Network.setBlockedURLs(ImmutableList.of("*.css", "*.png")));

        // Listening to events and check that the urls are actually blocked
		devTools.addListener(Network.loadingFailed(), loadingFailed -> {
			if (loadingFailed.getType().equals(ResourceType.STYLESHEET) ||
          loadingFailed.getType().equals(ResourceType.IMAGE)) {
        BlockedReason blockedReason = loadingFailed.getBlockedReason().orElse(null);
        Assertions.assertEquals(blockedReason, BlockedReason.INSPECTOR);
			}
		});

		webDriver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(Duration.ofSeconds(3).toMillis());

		// Disabling network interception and reloading the site
		devTools.send(Network.disable());
		webDriver.get("https://www.diemol.com/selenium-4-demo/relative-locators-demo.html");
		// Thread.sleep only meant for demo purposes!
		Thread.sleep(Duration.ofSeconds(3).toMillis());
	}

	@Test
	public void addExtraHeaders() throws InterruptedException {
		webDriver.get("https://manytools.org/http-html-text/http-request-headers/");
		
        // Thread.sleep only meant for demo purposes!
		Thread.sleep(Duration.ofSeconds(3).toMillis());

        devTools.send(new Command<>("Network.enable", ImmutableMap.of()));

		devTools.send(Network.setExtraHTTPHeaders(
			new Headers(ImmutableMap.of("selenium", "workshop"))));

		devTools.addListener(Network.loadingFailed(), loadingFailed -> {
			if (loadingFailed.getType().equals(ResourceType.STYLESHEET)) {
        BlockedReason blockedReason = loadingFailed.getBlockedReason().orElse(null);
				Assertions.assertEquals(blockedReason, BlockedReason.INSPECTOR);
			}
		});

		devTools.addListener(Network.requestWillBeSent(),
				requestWillBeSent ->
						Assertions.assertEquals(
							requestWillBeSent.getRequest().getHeaders().get("meetup"), "STUGRM"));

		devTools.addListener(Network.dataReceived(),
				dataReceived -> Assertions.assertNotNull(dataReceived.getRequestId()));

		webDriver.get("https://manytools.org/http-html-text/http-request-headers/");
		
        // Thread.sleep only meant for demo purposes!
		Thread.sleep(Duration.ofSeconds(3).toMillis());
	}

	@Test
	public void getFullPageScreenshot() throws IOException {
		webDriver.get("https://opensource.saucelabs.com/");

        GetLayoutMetricsResponse metrics = devTools
                .send(Page.getLayoutMetrics());
        Rect contentSize = metrics.getContentSize();
        String screenshotBase64 = devTools
                .send(Page.captureScreenshot(Optional.empty(), Optional.empty(),
                        Optional.of(new Viewport(0, 0, contentSize.getWidth(),
                                contentSize.getHeight(), 1)),
                        Optional.empty(), Optional.of(true),
                        Optional.of(false)));
        Path destination = Paths.get("fullpage-screenshot-chrome.png");
        Files.write(destination, Base64.getDecoder().decode(screenshotBase64));
	}

	@Test
	public void emulateTimezoneTest() throws InterruptedException {
		Map<String, Object> timezoneInfo = new HashMap<>();
		timezoneInfo.put("timezoneId", "America/Montevideo");

		devTools.send(new Command<>("Network.enable", ImmutableMap.of()));
		webDriver.executeCdpCommand("Emulation.setTimezoneOverride", timezoneInfo);
		webDriver.get("https://everytimezone.com/");

		// Thread.sleep only meant for demo purposes!
		Thread.sleep(Duration.ofSeconds(3).toMillis());
	}
}