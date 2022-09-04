package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class UserClient extends BaseApi {

    private static final String REG_PATH = "/auth/register";
    private static final String LOGIN_PATH = "/auth/login";
    private static final String AUTH_PATH = "/auth/user";

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return given()
                .spec(getRecSpec())
                .body(user)
                .when()
                .post(REG_PATH);
    }

    @Step("Авторизация пользователя")
    public static Response authUser(User user) {
        return given()
                .spec(getRecSpec())
                .body(user)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Обновление данных пользователя")
    public static Response updateUser(User user, String accessToken) {
        return given()
                .spec(getRecSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH_PATH);
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
        given()
                .spec(getRecSpec())
                .header("Authorization", accessToken)
                .delete(AUTH_PATH)
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED);
    }

    @Step("Получение токена пользователя")
    public static String getAccessToken(Response response) {
        return response
                .body()
                .jsonPath()
                .getString("accessToken");
    }
}
