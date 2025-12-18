package org.example.dao;

import org.example.dto.UserDto;
import org.h2.engine.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    // Сохранить пользователя
    void save(UserDto user);


    Optional<UserDto> findById(Long id);

   // List<User> findAll();

   // void deleteById(Long id);
}