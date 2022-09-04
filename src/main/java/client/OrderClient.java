package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Ingredients;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApi {

    private static final String ORDER_PATH = "/orders";
    private static final String INGREDIENT_PATH = "/ingredients";

    @Step("Создание заказа")
    public static Response createOrder(String accessToken, Ingredients ingredients) {
        return given()
                .spec(getRecSpec())
                .header("Authorization", accessToken)
                .body(ingredients)
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов пользователя")
    public static Response getOrders(String accessToken) {
        return given()
                .spec(getRecSpec())
                .header("Authorization", accessToken)
                .get(ORDER_PATH);
    }

    @Step("Получение списка ингридиентов")
    public static List<String> getIngredients() {
        return given()
                .spec(getRecSpec())
                .get(INGREDIENT_PATH)
                .then()
                .extract().path("data._id");
    }
}
