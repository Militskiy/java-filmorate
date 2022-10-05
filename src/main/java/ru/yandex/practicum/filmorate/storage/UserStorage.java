package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

public interface UserStorage extends Storage<User> {

    String FIND_ALL_QUERY =
            "SELECT * " +
            "FROM USERS";
    String FIND_USER_QUERY =
            "SELECT * " +
            "FROM USERS " +
            "WHERE USER_ID = ?";
    String CREATE_USER_QUERY =
            "INSERT INTO USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) " +
            "VALUES (?, ?, ?, ?)";
    String UPDATE_USER_QUERY =
            "UPDATE USERS " +
            "SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
            "WHERE USER_ID = ?";

    User create(User user);

    int update(User user);

    Optional<User> findById(Integer userId);
}
