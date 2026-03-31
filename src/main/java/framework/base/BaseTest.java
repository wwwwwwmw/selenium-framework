package framework.base;

import framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public abstract class BaseTest {
    // Dùng ThreadLocal để hỗ trợ chạy test song song an toàn
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        // Thiết lập biến môi trường để ConfigReader đọc đúng file .properties
        System.setProperty("env", env);

        WebDriver driver;

        // Khởi tạo browser tương ứng
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }

        ConfigReader config = ConfigReader.getInstance();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getExplicitWait()));
        driver.get(config.getBaseUrl());

        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Chụp ảnh màn hình nếu test case bị FAIL
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }

        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove(); // Xóa khỏi ThreadLocal để tránh memory leak
        }
    }

    private void captureScreenshot(String testName) {
        ConfigReader config = ConfigReader.getInstance();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";

        // Lấy đường dẫn từ file config (mặc định là target/screenshots/)
        String filePath = config.getScreenshotPath() != null ? config.getScreenshotPath() + fileName : "target/screenshots/" + fileName;

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        File destFile = new File(filePath);

        try {
            FileHandler.createDir(new File(config.getScreenshotPath() != null ? config.getScreenshotPath() : "target/screenshots/"));
            FileHandler.copy(srcFile, destFile);
            System.out.println("[Screenshot] Đã lưu ảnh lỗi tại: " + filePath);
        } catch (IOException e) {
            System.out.println("Không thể lưu ảnh chụp màn hình: " + e.getMessage());
        }
    }
}