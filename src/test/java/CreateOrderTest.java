import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredients;
import org.junit.Test;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTest extends BaseTest {

    Response responseOrder;
    OrderClient orderClient;
    List<String> ingredients = orderClient.getIngredients();

    public static final String BAD_REQUEST = "Ingredient ids must be provided";

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrder() {
        responseOrder = orderClient.createOrder(BaseTest.accessToken, (new Ingredients(new String[]{ingredients.get(0), ingredients.get(1)})));

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_OK, responseOrder.statusCode());
        assertNotNull("Номер заказа пуст", responseOrder.body().path("order.number"));
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void createOrderUnauthorized() {
        responseOrder = orderClient.createOrder("", (new Ingredients(new String[]{ingredients.get(0), ingredients.get(1)})));

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_OK, responseOrder.statusCode());
        assertNotNull("Номер заказа пуст", responseOrder.body().path("order.number"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        responseOrder = orderClient.createOrder(BaseTest.accessToken, new Ingredients(new String[]{}));

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_BAD_REQUEST, responseOrder.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", BAD_REQUEST, responseOrder.body().path("message"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшом ингредиентов")
    public void createOrderInvalidIngredients() {
        responseOrder = orderClient.createOrder(BaseTest.accessToken, new Ingredients(new String[]{ingredients.get(0) + 0, ingredients.get(1)}));

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_INTERNAL_SERVER_ERROR, responseOrder.statusCode());
    }
}
