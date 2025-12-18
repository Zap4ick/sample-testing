package org.example.api;

import io.restassured.http.ContentType;
import org.example.dto.UserDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserApiTest {

    @Test
    void testCreateAndGetUser() {
        baseURI = "https://jsonplaceholder.typicode.com";

        UserDto newUser = new UserDto(11L, "QA Automation Engineer");

        given()
                .contentType(ContentType.JSON)
                .body(newUser) // Сериализация объекта в JSON
                .when()
                .post("/users")
                .then()
                .statusCode(201) // Проверка HTTP статуса
                .body("name", equalTo("QA Automation Engineer"));

        // 2. GET - Получаем пользователя
        given()
                .pathParam("id", 1)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("username", notNullValue());
    }
}