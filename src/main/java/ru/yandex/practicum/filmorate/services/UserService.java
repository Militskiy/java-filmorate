package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Qualifier("UserDaoImpl")
    private final UserDao userStorage;

    public Collection<User> findAllUsers() {
        log.debug("Sending user list");
        return userStorage.findAll();
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Adding new user: {}", user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        log.debug("Updated user with ID: {}", user.getId());
        return userStorage.update(user);
    }

    public int removeUser(Integer userId) {
        log.info("User deleted");
        return userStorage.removeUser(userId);
    }

    public void addFriends(Integer userId, Integer friendId) {
        if (userStorage.createFriend(userId, friendId)) {
            log.debug("Creating friend link between users with ids: {} and {}", userId, friendId);
        } else {
            throw new BadArgumentsException("Users are already friends");
        }
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        if (!userStorage.delete(userId, friendId)) {
            throw new BadArgumentsException("Users are not friends");
        }
    }

    public Collection<User> findFriends(Integer userId) {
        return userStorage.findFriends(userId);
    }

    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        log.debug("Listing common friends for users with ids {} and {}", userId, otherId);
        return userStorage.findCommonFriends(userId, otherId);
    }

    public User findUserById(Integer userId) {
        return userStorage.findById(userId);
    }
}
