import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import org.junit.After;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    String email = "garayevas@yandex.ru";
    String password = "123456";
    String name = "Sairis";
    String incorrectEmail = "";
    String accessToken;
    UserSteps userSteps;
    private void createUserAndCheck(User user) {
        Response createUserResponse = userSteps.createUser(user);
        createUserResponse.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
        Response loginResponse = userSteps.loginUser(user);
        loginResponse.then().assertThat()
                .body("accessToken", notNullValue())
                .and()
                .statusCode(200);
        accessToken = loginResponse.body().jsonPath().getString("accessToken");
    }

    @Before
    public void setUp() {

        RestAssured.baseURI = Api.URL;
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Cоздать уникального пользователя")
    public void createUserTest() {
        User user = new User(email, password, name);
        createUserAndCheck(user);
    }

    @Test
    @DisplayName("Cоздать пользователя, который уже зарегистрирован")
    public void createDuplicateTest() {
        User user = new User(email, password, name);
        createUserAndCheck(user);
        Response response = userSteps.createUser(user);
        response.then().assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }
    @Test
    @DisplayName("Cоздать пользователя и не заполнить одно из обязательных полей")
    public void createIncorrectTest() {
        User user = new User(incorrectEmail, password, name);
        Response response = userSteps.createUser(user);
        response.then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }
}

