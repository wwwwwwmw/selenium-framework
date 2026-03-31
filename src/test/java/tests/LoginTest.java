package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Quản lý Đăng nhập")
public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return ExcelReader.getData("src/test/resources/testdata/login_data.xlsx", "SmokeCases");
    }

    @Test(dataProvider = "loginData", description = "Đăng nhập từ file Excel", priority = 1)
    @Story("Đăng nhập bằng dữ liệu Excel")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithExcel(String username, String password, String expectedUrl, String desc) {
        LoginPage loginPage = new LoginPage(getDriver());

        Allure.step("Nhập username: " + username);
        Allure.step("Nhập password: " + password);
        loginPage.login(username, password);

        Allure.step("Kiểm tra URL chứa: " + expectedUrl);
        Assert.assertTrue(getDriver().getCurrentUrl().contains(expectedUrl), "Thất bại: " + desc);
    }

    @Test(description = "BÀI 3: Đăng nhập bằng tài khoản bảo mật (GitHub Secrets)", priority = 2)
    @Story("Đăng nhập bảo mật")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Sử dụng GitHub Secrets để bảo vệ thông tin đăng nhập nhạy cảm")
    public void testLoginWithSecrets() {
        LoginPage loginPage = new LoginPage(getDriver());

        String username = Allure.step("Lấy Username từ hệ thống", () -> ConfigReader.getInstance().getUsername());
        String password = Allure.step("Lấy Password từ hệ thống", () -> ConfigReader.getInstance().getPassword());

        Allure.step("Thực hiện đăng nhập");
        loginPage.login(username, password);

        Allure.step("Xác nhận chuyển vào trang chủ Inventory");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "Đăng nhập bằng Secret thất bại");
    }
}