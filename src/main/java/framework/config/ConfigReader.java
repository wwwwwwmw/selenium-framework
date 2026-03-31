package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try {
            // Đọc file config tương ứng với môi trường (mặc định là dev)
            String env = System.getProperty("env", "dev");
            String configFilePath = "src/test/resources/config-" + env + ".properties";
            FileInputStream fileInputStream = new FileInputStream(configFilePath);
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Không tìm thấy file cấu hình: " + e.getMessage());
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Lấy URL cơ sở của ứng dụng [cite: 22, 221]
     */
    public String getBaseUrl() {
        return getProperty("base.url");
    }

    /**
     * Lấy đường dẫn lưu ảnh chụp màn hình [cite: 44, 85]
     */
    public String getScreenshotPath() {
        return getProperty("screenshot.path");
    }

    /**
     * Lấy số lần chạy lại (Retry) khi test thất bại
     */
    public int getRetryCount() {
        String count = getProperty("retry.count");
        return (count != null) ? Integer.parseInt(count) : 0;
    }

    /**
     * Lấy Username (Ưu tiên GitHub Secrets cho Bài 3) [cite: 21, 199]
     */
    public String getUsername() {
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            username = getProperty("app.username");
        }
        return username;
    }

    /**
     * Lấy Password (Ưu tiên GitHub Secrets cho Bài 3) [cite: 21, 200]
     */
    public String getPassword() {
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            password = getProperty("app.password");
        }
        return password;
    }
}