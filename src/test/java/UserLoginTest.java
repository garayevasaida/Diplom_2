import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import org.junit.After;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {
    String email = "garayevas@yandex.ru";
    String password = "123456";
    String name = "Sairis";
    String accessToken;
    UserSteps userSteps;



    @Before
    public void setUp() {
        RestAssured.baseURI = Api.URL;
        User user = new User(email, password, name);
        userSteps = new UserSteps();
        Response responseAccessToken = userSteps.createUser(user);
        responseAccessToken.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        this.accessToken = responseAccessToken.body().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Логин под существующим пользователем,")
    public void loginUserTest() {
        User user = new User("garayevas@yandex.ru", "123456", "Sairis");
        userSteps.loginUser(user)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Логин с неверными данными")
    public void loginWithIncorrecTest() {
        User user = new User("garayevas@gmail.com", "54684158", "Irina");
        userSteps.loginUser(user)
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }
}

