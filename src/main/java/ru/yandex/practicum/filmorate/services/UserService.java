package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        if (userStorage.update(user) == 1) {
            log.debug("Updating user with ID: {}", user.getId());
            return user;
        } else {
            throw new NoSuchUserException("No user with such ID: " + user.getId());
        }
    }

    // проверки в friendStorage, c целью не использовать доп запросы
    public void addFriends(Integer userId, Integer friendId) {
        userStorage.createFriend(userId, friendId);
        log.debug("Creating friend link between users with ids: {} and {}", userId, friendId);
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        if (!userStorage.delete(userId, friendId)) {
            throw new BadArgumentsException("Users are not friends");
        }
    }

    // проверка наличия пользователя без доп SQL запросов от findById
    public Collection<User> findFriends(Integer userId) {
        if (userStorage.findById(userId).isPresent()) {
            return userStorage.findFriends(userId);
        } else {
            throw new NoSuchUserException("No user with such ID: "+ userId);
        }
    }
    // проверка наличия пользователя без доп SQL запросов от findById
    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        List<User> result = new ArrayList<>(userStorage.findCommonFriends(userId, otherId));
        if(result.get(0).getId() == userId || result.get(1).getId() == userId) {
            if (result.get(1).getId() == otherId || result.get(0).getId() == otherId) {
                log.debug("Listing common friends for users with ids {} and {}", userId, otherId);
                return result.stream().skip(2).collect(Collectors.toList());
            } else {
                throw new NoSuchUserException("No user with such ID: " + otherId);
            }
        } else {
            throw new NoSuchUserException("No user with such ID: " + userId);
        }
    }

    public User findUserById(Integer userId) {
        return userStorage.findById(userId).orElseThrow(
                () -> new NoSuchUserException("No user with such ID: " + userId));
    }
}
