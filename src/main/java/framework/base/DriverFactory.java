package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        String gridUrl = System.getProperty("grid.url");
        boolean isCI = System.getenv("CI") != null;

        if (gridUrl != null && !gridUrl.isBlank()) {
            return createRemoteDriver(browser, gridUrl);
        }
        return createLocalDriver(browser, isCI);
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fOptions = new FirefoxOptions();
                if (headless) fOptions.addArguments("-headless");
                return new FirefoxDriver(fOptions);
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions cOptions = new ChromeOptions();
                if (headless) {
                    cOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                }
                cOptions.addArguments("--start-maximized");
                return new ChromeDriver(cOptions);
        }
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browser.toLowerCase());

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            caps.merge(options);
        }

        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Grid URL không hợp lệ: " + gridUrl);
        }
    }
}