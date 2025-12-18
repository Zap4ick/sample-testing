package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.UserDao;
import org.example.dto.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class IntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static UserDao userDao;
    private static Connection connection;

    @BeforeAll
    static void startServer() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        connection.createStatement().execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255))");

        userDao = new UserDao(connection);

        Spark.port(4567);

        Spark.post("/users", (req, res) -> {
            UserDto user = mapper.readValue(req.body(), UserDto.class);
            userDao.save(user);
            res.status(201);
            res.type("application/json");
            return mapper.writeValueAsString(user);
        });

        Spark.get("/users/:id", (req, res) -> {
            Long id = Long.parseLong(req.params(":id"));

            return userDao.findById(id)
                    .map(u -> {
                        res.status(200);
                        try { return mapper.writeValueAsString(u); }
                        catch (Exception e) { return ""; }
                    })
                    .orElseGet(() -> {
                        res.status(404);
                        return "{}";
                    });
        });

        Spark.awaitInitialization();
    }

    @AfterAll
    static void stopServer() {
        Spark.stop();
    }

    @Test
    void testRealInternalApi() throws JsonProcessingException {
        UserDto user = new UserDto(10L, "RealDBUser");

        given()
                .port(4567)
                .body(mapper.writeValueAsString(user))
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("RealDBUser"));
    }

    @Test
    void testCreateUserInRealDbViaApi() {
        UserDto newUser = new UserDto(1L, "Ivan");

        given()
                .port(4567)
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        UserDto savedUser = userDao.findById(1L).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo("Ivan");
    }
}
