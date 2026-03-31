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
            e.printStackTrace();
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

    // --- LOGIC CHO BÀI 3: ĐỌC GITHUB SECRETS ---
    public String getUsername() {
        // Ưu tiên 1: Đọc từ biến môi trường (khi chạy trên GitHub Actions)
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            // Ưu tiên 2: Fallback đọc từ file properties (khi chạy ở máy Local)
            username = getProperty("app.username");
        }
        return username;
    }

    public String getPassword() {
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            password = getProperty("app.password");
        }
        return password;
    }
}