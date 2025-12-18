package org.example.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@WireMockTest(httpPort = 8080) // Запускает сервер на localhost:8080
public class LocalApiTest {

    @Test
    void testLocalGetEndpoint() {
        stubFor(get(urlEqualTo("/user/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"LocalQA\"}")));

        given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/user/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("LocalQA"));
    }
}
