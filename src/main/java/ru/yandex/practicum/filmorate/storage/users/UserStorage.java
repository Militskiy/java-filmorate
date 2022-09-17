package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAllUsers();

    void createUser(User user);

    void updateUser(User user);

    Optional<User> findUserById(Integer id);
}
