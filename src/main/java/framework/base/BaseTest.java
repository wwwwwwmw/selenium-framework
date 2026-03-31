package framework.base;

import framework.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.qameta.allure.Attachment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driverThread.get();
    }

    @BeforeMethod
    public void setUp() {
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = ConfigReader.getInstance().getProperty("browser");
        }
        if (browser == null) browser = "chrome";

        WebDriver driver = DriverFactory.createDriver(browser);
        driverThread.set(driver);

        // Sử dụng getBaseUrl() đã fix lỗi Cannot Resolve
        getDriver().get(ConfigReader.getInstance().getBaseUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            attachScreenshot();
        }
        if (getDriver() != null) {
            getDriver().quit();
        }
        driverThread.remove();
    }

    @Attachment(value = "Ảnh chụp khi thất bại", type = "image/png")
    public byte[] attachScreenshot() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    private void captureScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Sử dụng getScreenshotPath() đã fix lỗi Cannot Resolve
        String folderPath = ConfigReader.getInstance().getScreenshotPath();
        String filePath = folderPath + testName + "_" + timestamp + ".png";

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES).length > 0 ?
                ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE) : null;

        if (srcFile != null) {
            try {
                FileUtils.copyFile(srcFile, new File(filePath));
                System.out.println("Chụp màn hình thành công: " + filePath);
            } catch (IOException e) {
                System.err.println("Lỗi khi lưu ảnh chụp màn hình: " + e.getMessage());
            }
        }
    }
}