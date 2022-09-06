package first_api_test;

import modules.GetSingleUserResponse;
import modules.User;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static modules.Constants.baseUrl;
import static modules.Constants.userUrl;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void shouldGetUser() {
        String getUserEndpoint = baseUrl + userUrl + "?page=2";
        given()
                .when()
                .get(getUserEndpoint)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("page", equalTo(2))
                .body("per_page", greaterThan(5))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data", notNullValue());
    }

    @Test
    public void shouldGetSpecificUserLastName() {
        String getUserEndpoint = baseUrl + userUrl + "?page=2";
        given()
                .when()
                .get(getUserEndpoint)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.last_name[0]", equalTo("Lawson"));
    }

    @Test
    public void shouldGetUserHeaderResponse() {
        String getUserEndpoint = baseUrl + userUrl + "?page=2";
        given()
                .when()
                .get(getUserEndpoint)
                .then()
                .log()
                .headers()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .header("Content-Type", equalTo("application/json; charset=utf-8"));
    }

    @Test
    public void shouldRegisterNewUser() {
        String getUserEndpoint = baseUrl + userUrl;
        User user = new User(
                "Yura",
                "Bazhan"
        );
        var response = given().body(user).when().post(getUserEndpoint).then();
        response.log().body();

        Assertions.assertEquals(user.getName(), "Yura", "New User was created with not correct name");
    }

    @Test
    public void shouldUpdateUser() {
        String getUserEndpoint = baseUrl + userUrl + "/2";
        String body = """
                {
                "name": "morpheus",
                "job": "zion resident"
                }
                """;
        var response = given().body(body).when().put(getUserEndpoint).then();
        response.log().body();
    }

    @Test
    public void shouldDeleteUser() {
        String getUserEndpoint = baseUrl + userUrl + "/2";
        User user = new User(
                "Yura",
                "Bazhan"
        );
        var response = given().body(user).when().delete(getUserEndpoint).then();
        response.log().body();
    }

    @Test
    public void shouldReturnSingleUser() {
        String getUserEndpoint = baseUrl + userUrl + "/2";

        GetSingleUserResponse getUserResponse = given().when().get(getUserEndpoint)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().body().as(GetSingleUserResponse.class);

        Assertions.assertNotNull(getUserResponse.getData(), "The null was returned instead of user object");
        Assertions.assertNotNull(getUserResponse.getSupport(), "The null was returned instead of user object");
    }
}
