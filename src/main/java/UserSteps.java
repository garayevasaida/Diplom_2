import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given().spec(Api.baseRequestSpec())
                .body(user)
                .when()
                .post(Api.CREATE_USER)
                .thenReturn();
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return given().spec(Api.baseRequestSpec())
                .body(user)
                .when()
                .post(Api.LOGIN_USER).thenReturn();

    }
    @Step("Изменение данных пользователя с авторизацией")
    public Response changeUserWithToken(User user, String accessToken){
        return given().spec(Api.baseRequestSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(Api.CHANGE_USER);

    }

    @Step("Изменение данных пользователя без авторизации")
    public Response changeUserWithoutToken(User user){
        return given().spec(Api.baseRequestSpec())
                .body(user)
                .when()
                .patch(Api.CHANGE_USER);
    }
    @Step("Удалить пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given().spec(Api.baseRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(Api.DELETE_USER)
                .then()
                .assertThat()
                .statusCode(202);
    }

}

