import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithToken(Order order, String accessToken) {
        return given().spec(Api.baseRequestSpec())
                .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post(Api.CREATE_ORDER);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutToken(Order order) {
        return given().spec(Api.baseRequestSpec())
                .body(order)
                .when()
                .post(Api.CREATE_ORDER);
    }

    @Step("Получение заказов авторизованного пользователя")
    public Response getOrderWithToken(String accessToken) {
        return given().spec(Api.baseRequestSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(Api.CREATE_ORDER);
    }
    @Step("Получение заказов не авторизованного пользователя")
    public Response getOrderWithoutToken() {
        return given().spec(Api.baseRequestSpec())
                .when()
                .get(Api.CREATE_ORDER)
                .thenReturn();
    }
    @Step("Создания заказа с невалидным хешем ингредиентов")
    public Response createOrderWithInvalidIngredients(String accessToken, List<String> invalidIngredients) {
        Order order = new Order(invalidIngredients);
        return given().spec(Api.baseRequestSpec())
                .header("Authorization", accessToken)
                .body(order)
                .post(Api.CREATE_ORDER)
                .thenReturn();
    }
    @Step("Получить данные об ингредиентах")
    public Response getIngredient() {
        return given()
                .get(Api.INGREDIENTS);
    }
}

