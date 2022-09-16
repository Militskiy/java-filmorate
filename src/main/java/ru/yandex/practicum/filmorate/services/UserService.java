package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;
import ru.yandex.practicum.filmorate.utils.UserValidator;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private Integer userId = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        log.debug("Sending user list");
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        UserValidator.validateUser(user);
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.createUser(user);
        log.debug("Added new user: {}", user);
        return user;
    }

    public User updateUser(User user) {
        UserValidator.validateUser(user);
        if (userStorage.findUserById(user.getId()).isEmpty()) {
            throw new NoSuchUserException("No user with such ID");
        }
        userStorage.updateUser(user);
        log.debug("Edited user with id: {} and name: {}", user.getId(), user.getName());
        return user;
    }

    public User addFriends(Integer userId, Integer friendId) {
        if (userStorage.findUserById(userId).isEmpty()) {
            throw new NoSuchUserException("No user with such ID");
        }

        if (userStorage.findUserById(friendId).isEmpty()) {
            throw new NoSuchUserException("No friend with such ID");
        }
        if (userStorage.findUserById(friendId).get().addFriend(userId)) {
            throw new BadArgumentsException("Users are already friends");
        }
        if (userStorage.findUserById(userId).get().addFriend(friendId)) {
            throw new BadArgumentsException("Users are already friends");
        }
        log.debug("Users {} and {} are now friends",
                userStorage.findUserById(userId).get().getName(),
                userStorage.findUserById(friendId).get().getName());
        return userStorage.findUserById(userId).get();
    }

    public User deleteFriends(Integer userId, Integer friendId) {
        if (userStorage.findUserById(userId).isEmpty()) {
            throw new NoSuchUserException("No user with such ID");
        }

        if (userStorage.findUserById(friendId).isEmpty()) {
            throw new NoSuchUserException("No friend with such ID");
        }
        if (userStorage.findUserById(friendId).get().deleteFriend(userId)) {
            throw new BadArgumentsException("Users are not friends");
        }
        if (userStorage.findUserById(userId).get().deleteFriend(friendId)) {
            throw new BadArgumentsException("Users are not friends");
        }
        log.debug("Users {} and {} are no longer friends",
                userStorage.findUserById(userId).get().getName(),
                userStorage.findUserById(friendId).get().getName());
        return userStorage.findUserById(userId).get();
    }

    public Collection<User> findFriends(Integer userId) {
        log.debug("Listing {}'s friends", userStorage.findUserById(userId).orElseThrow(
                        () -> new NoSuchUserException("No user with Such ID"))
                .getName()
        );
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NoSuchUserException("No user with such ID")).getFriends()
                .stream().map(id -> userStorage.findUserById(id).orElse(null))
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        log.debug("Listing {}'s and {}'s common friends",
                userStorage.findUserById(userId).orElseThrow(
                        () -> new NoSuchUserException("No user with Such ID")).getName(),
                userStorage.findUserById(otherId).orElseThrow(
                        () -> new NoSuchUserException("No other user with such ID")).getName()
        );
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NoSuchUserException("No user with such ID")).getFriends().stream()
                .filter(id -> userStorage.findUserById(otherId)
                        .orElseThrow(() -> new NoSuchUserException("No other user with such ID"))
                        .getFriends().contains(id))
                .map(commonId -> userStorage.findUserById(commonId).orElse(null))
                .collect(Collectors.toList());
    }

    public User findUser(Integer userId) {
        return userStorage.findUserById(userId).orElseThrow(
                () -> new NoSuchUserException("No user with such ID"));
    }

    private Integer getNextId() {
        return ++userId;
    }
}
