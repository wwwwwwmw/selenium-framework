package framework.utils;

import framework.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        // Đọc số lần retry tối đa từ file config (Bài 5 & 6 kết hợp)
        int maxRetry = ConfigReader.getInstance().getRetryCount();
        if (retryCount < maxRetry) {
            retryCount++;
            System.out.println("[Retry] Đang chạy lại lần " + retryCount + " cho test: " + result.getName());
            return true; // Cho phép chạy lại
        }
        return false; // Dừng lại và đánh dấu FAIL
    }
}