package tests;

import framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlakySimulationTest extends BaseTest {
    private static int count = 0;

    @Test(description = "Mô phỏng test lỗi 2 lần đầu")
    public void testFlaky() {
        count++;
        if (count <= 2) {
            Assert.fail("Lỗi mạng giả lập lần " + count);
        }
        Assert.assertTrue(true);
    }
}