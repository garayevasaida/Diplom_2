import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CreateOrderTest {
    String email = "garayevas@yandex.ru";
    String password = "123456";
    String name = "Sairis";
    OrderSteps orderSteps;
    UserSteps userSteps;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.URL;
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
        User user = new User(email, password, name);
        Response responseAccessToken = userSteps.createUser(user);
        responseAccessToken.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        this.accessToken = responseAccessToken.body().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderTest() {
        Response getIngredient = orderSteps.getIngredient();
        List<String> ingredients = new ArrayList<>(getIngredient.then().log().all().statusCode(200).extract().path("data._id"));
        Order order = new Order(ingredients);
        Response response = orderSteps.createOrderWithToken(order,accessToken);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")  // в группе обсуждалось с наставником, что и без авторизации приходит 200
    public void createOrderWithoutTokenTest() {
        Response getIngredient = orderSteps.getIngredient();
        List<String> ingredients = new ArrayList<>(getIngredient.then().log().all().statusCode(200).extract().path("data._id"));
        Order order = new Order(ingredients);
        Response response = orderSteps.createOrderWithoutToken(order);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }


    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order(null);
        orderSteps.createOrderWithToken(order, accessToken)
                .then().assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }


    @Test
    @DisplayName("Создание заказа с невалидными ингредиентами")
    public void createOrderWithIncorrectIngredientTest() {
        List<String> invalidIngredients = Arrays.asList("notAValidIngredientId");
        Response response = orderSteps.createOrderWithInvalidIngredients(accessToken, invalidIngredients);
        assertEquals(500, response.getStatusCode());
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }
}





