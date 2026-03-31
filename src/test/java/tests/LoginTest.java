package tests;

import framework.base.BaseTest;
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

    @Test(dataProvider = "loginData", description = "Đăng nhập từ file Excel")
    public void testLoginWithExcel(String username, String password, String expectedUrl, String desc) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);
        Assert.assertTrue(getDriver().getCurrentUrl().contains(expectedUrl), "Thất bại: " + desc);
    }
}