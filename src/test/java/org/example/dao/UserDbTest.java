package org.example.dao;

import org.example.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class UserDbTest {
    private Connection connection;
    private UserDao userDAO;

    @BeforeEach
    void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        connection.createStatement().execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255))");
        userDAO = new UserDao(connection);
    }

    @Test
    void testSaveUser() {
        UserDto user = new UserDto(1L, "Ivan");
        userDAO.save(user);

        UserDto userFromDb = userDAO.findById(1L).orElseThrow();
        assertThat(userFromDb).isEqualTo(user);
        // Здесь мы бы использовали findById для проверки (Assertion)
        Assertions.assertNotNull(user);
    }
}