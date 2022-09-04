import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.junit.*;

import static client.UserClient.*;

public class BaseTest {

    static User user;
    static String accessToken;
    UserClient userClient;
    Response responseCreate;

    @Before
    public void data() {
        user = user.getRandomUser();
        responseCreate = createUser(user);
        user = new User(user.getEmail(), user.getPassword(), user.getName());
        accessToken = getAccessToken(responseCreate);
    }

    @After
    public void deleteUser() {
        if (responseCreate.statusCode() == 200) {
            accessToken = getAccessToken(responseCreate);
            userClient.deleteUser(accessToken);
        }
    }
}
