package first_api_test;

import modules.Users;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void getUser() {
        String endpoint = "https://reqres.in/api/users?page=2";
        given().
                when().
                get(endpoint).
                then().
                assertThat().
                statusCode(200).
                body("page", equalTo(2)).
                body("per_page", greaterThan(5)).
                body("total", equalTo(12)).
                body("total_pages", equalTo(2)).
                body("data", notNullValue());
    }

    @Test
    public void getSpecificUserLastName() {
        String endpoint = "https://reqres.in/api/users?page=2";
        given().
                when().
                get(endpoint).
                then().
                assertThat().
                statusCode(200).
                body("data.last_name[0]", equalTo("Lawson"));
    }

    @Test
    public void getUserHeaderResponse() {
        String endpoint = "https://reqres.in/api/users?page=2";
        given().
                when().
                get(endpoint).
                then().
                log().
                headers().
                assertThat().
                statusCode(200).
                header("Content-Type", equalTo("application/json; charset=utf-8"));
    }

    @Test
    public void registerNewUser() {
        String endpoint = "https://reqres.in/api/users";
        Users user = new Users(
                "Yura",
                "Bazhan"
        );
        var response = given().body(user).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateUser() {
        String endpoint = "https://reqres.in/api/users/2";
        String body = """
                {
                "name": "morpheus",
                "job": "zion resident"
                }
                """;
        var response = given().body(body).when().put(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteUser() {
        String endpoint = "https://reqres.in/api/users/2";
        Users user = new Users(
                "Yura",
                "Bazhan"
        );
        var response = given().body(user).when().delete(endpoint).then();
        response.log().body();
    }
}
