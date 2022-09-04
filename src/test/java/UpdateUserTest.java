import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static client.UserClient.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UpdateUserTest extends BaseTest {

    Response responseUpdate;

    String email = RandomStringUtils.randomAlphabetic(6) + "@test.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    public final static String UNAUTHORISED_USER = "You should be authorised";

    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    public void updateEmailAuthUserTest() {
        responseUpdate = updateUser(new User(email, password, name), BaseTest.accessToken);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_OK, responseUpdate.statusCode());
        assertTrue("Ожидаемый ответ не соответсвует фактическому", responseUpdate.body().jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя")
    public void updateUnauthorizedUserTest() {
        responseUpdate = updateUser(new User(email, password, name), "");

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_UNAUTHORIZED, responseUpdate.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", UNAUTHORISED_USER, responseUpdate.body().jsonPath().getString("message"));
    }
}
