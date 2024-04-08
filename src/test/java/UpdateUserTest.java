import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.Matchers.*;

public class UpdateUserTest {
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
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void changeUserNameTest() {
        User userNew = new User(email, password, "Irina"); // Изменяем имя
        Response userForChange = userSteps.changeUserWithToken(userNew, accessToken);
        userForChange.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    public void changeUserPasswordTest() {
        User userNew = new User(email, "654321", name); // Изменяем пароль
        Response userForChange = userSteps.changeUserWithToken(userNew, accessToken);
        userForChange.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    public void changeUserEmailTest() {
        User userNew = new User("gareyvas@gmail.com", password, name); // Изменяем email
        Response userForChange = userSteps.changeUserWithToken(userNew, accessToken);
        userForChange.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserWithoutTokenTest() {
        User userNew = new User(email, password, name); // Изменяем без авторизации
        Response userForChange = userSteps.changeUserWithoutToken(userNew);
        userForChange.then().assertThat().body("message", equalTo("You should be authorised"))
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


