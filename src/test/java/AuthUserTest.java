import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static client.UserClient.authUser;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class AuthUserTest extends BaseTest {

    Response responseAuth;

    public final static String INVALID_DATA = "email or password are incorrect";

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void authUserTest() {
        responseAuth = authUser(BaseTest.user);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_OK, responseAuth.statusCode());
        assertNotNull("Поле accessToken пустое", responseAuth.body().path("accessToken"));
    }

    @Test
    @DisplayName("Авторизация с неверной почтой")
    public void authUserWithInvalidLoginTest() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10));
        responseAuth = authUser(BaseTest.user);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_UNAUTHORIZED, responseAuth.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", INVALID_DATA, responseAuth.body().path("message"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void authUserWithInvalidPasswordTest() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        responseAuth = authUser(BaseTest.user);

        assertEquals("Ожидаемый и фактический код ответа не совпадают",SC_UNAUTHORIZED, responseAuth.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", INVALID_DATA, responseAuth.body().path("message"));
    }
}
