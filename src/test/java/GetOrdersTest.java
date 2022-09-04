import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class GetOrdersTest extends BaseTest {

    OrderClient orderClient;
    Response responseList;

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getOrder() {
        responseList = orderClient.getOrders(BaseTest.accessToken);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_OK, responseList.statusCode());
        assertTrue("Ожидаемый ответ не соответсвует фактическому", responseList.body().jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованного пользователя")
    public void getOrderUnauthorized() {
        responseList = orderClient.getOrders("");

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_UNAUTHORIZED, responseList.statusCode());
        assertFalse("Ожидаемый ответ не соответсвует фактическому", responseList.body().jsonPath().getBoolean("success"));
    }
}
