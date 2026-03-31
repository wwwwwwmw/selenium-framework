package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return ExcelReader.getData("src/test/resources/testdata/login_data.xlsx", "SmokeCases");
    }

    @Test(dataProvider = "loginData", description = "Đăng nhập từ file Excel", priority = 1)
    public void testLoginWithExcel(String username, String password, String expectedUrl, String desc) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);
        Assert.assertTrue(getDriver().getCurrentUrl().contains(expectedUrl), "Thất bại: " + desc);
    }

    // --- THÊM TEST CHO BÀI 3 ---
    @Test(description = "BÀI 3: Đăng nhập bằng tài khoản bảo mật (GitHub Secrets)", priority = 2)
    public void testLoginWithSecrets() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Gọi ConfigReader để tự động phân luồng lấy Data (Local file hoặc GitHub Secret)
        String username = ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();

        loginPage.login(username, password);

        // Xác nhận đăng nhập thành công (Saucedemo khi login xong sẽ có url chứa chữ 'inventory')
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "Đăng nhập bằng Secret thất bại");
    }
}