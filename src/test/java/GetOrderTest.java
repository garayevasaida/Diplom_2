import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GetOrderTest {
    String email = "garayevas@yandex.ru";
    String password = "123456";
    String name = "Sairis";
    private OrderSteps orderSteps;
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.URL;
        User user = new User(email, password, name);
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        Response responseAccessToken = userSteps.createUser(user);
        responseAccessToken.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        this.accessToken = responseAccessToken.body().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Получение данных заказов пользователя с авторизацией")
    public void getOrdersWithTokenTest() {
        Response response = orderSteps.getOrderWithToken(accessToken);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Получение данных заказов пользователя без авторизации")
    public void getOrderWithoutTokenTest() {
        Response response = orderSteps.getOrderWithoutToken();
        assertEquals(401, response.getStatusCode());
        assertFalse(response.getBody().jsonPath().getBoolean("success"));
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }
}
