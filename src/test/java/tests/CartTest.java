package tests;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import framework.utils.TestDataFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class CartTest extends BaseTest {

    @Test(description = "Quy trình mua hàng với dữ liệu Faker")
    public void testCheckoutProcess() {
        LoginPage login = new LoginPage(getDriver());

        // Sử dụng Fluent Interface
        int itemCount = login.login("standard_user", "secret_sauce")
                .addFirstItemToCart()
                .getCartItemCount();

        Assert.assertEquals(itemCount, 1);

        // Sinh data ngẫu nhiên cho Bài 4B
        Map<String, String> customer = TestDataFactory.randomCheckoutData();
        System.out.println("Thanh toán cho khách hàng: " + customer.get("firstName"));
    }
}