import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.*;

import static client.UserClient.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateUserTest {

    Response responseCreate;
    User user;
    UserClient userClient;
    String accessToken;

    public final static String ALREADY_EXIST = "User already exists";
    public final static String NO_REQUIRED_FIELDS = "Email, password and name are required fields";

    @Before
    public void data() {
        user = user.getRandomUser();
    }

    @After
    public void deleteUser() {
        if (responseCreate.statusCode() == 200) {
            accessToken = getAccessToken(responseCreate);
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        responseCreate = createUser(user);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_OK, responseCreate.statusCode());
        assertNotNull("Поле accessToken пустое", responseCreate.body().path("accessToken"));
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createAlreadyRegUserTest() {
        responseCreate = createUser(user);
        Response responseAgainCreate = createUser(user);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_FORBIDDEN, responseAgainCreate.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", ALREADY_EXIST, responseAgainCreate.body().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        user.setName("");
        responseCreate = createUser(user);

        assertEquals("Ожидаемый и фактический код ответа не совпадают", SC_FORBIDDEN, responseCreate.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", NO_REQUIRED_FIELDS, responseCreate.body().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword("");
        responseCreate = createUser(user);

        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", NO_REQUIRED_FIELDS, responseCreate.body().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        responseCreate = createUser(user);

        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());
        assertEquals("Ожидаемый ответ не соответсвует фактическому", NO_REQUIRED_FIELDS, responseCreate.body().path("message"));
    }
}
