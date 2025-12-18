package org.example.dao;

import org.example.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao implements UserRepository {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(UserDto user) {
        String sql = "INSERT INTO users (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    UserDto user = new UserDto(
                            rs.getLong("id"),
                            rs.getString("name")
                    );
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding user with id id: " + id, e);
        }

        return Optional.empty();
    }
}