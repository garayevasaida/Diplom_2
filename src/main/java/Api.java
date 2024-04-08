import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Api {
    public static final String URL = "https://stellarburgers.nomoreparties.site/";
    public static final String CREATE_USER = "api/auth/register";
    public static final String CHANGE_USER = "api/auth/user";
    public static final String LOGIN_USER = "api/auth/login";
    public static final String CREATE_ORDER = "/api/orders";
    public static final String INGREDIENTS = "api/ingredients";
    public static final String DELETE_USER = "/api/auth/user";

    protected static RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }
}
