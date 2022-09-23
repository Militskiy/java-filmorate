package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private Integer userId = 0;

    public Collection<User> findAllUsers() {
        log.debug("Sending user list");
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.createUser(user);
        log.debug("Added new user: {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (userStorage.findUserById(user.getId()).isPresent()) {
            userStorage.updateUser(user);
            log.debug("Edited user with id: {} and name: {}", user.getId(), user.getName());
            return user;
        } else {
            throw new NoSuchUserException("No user with such ID: " + user.getId());
        }
    }

    public User addFriends(Integer userId, Integer friendId) {
        if (findUserById(friendId).addFriend(userId) &&
                findUserById(userId).addFriend(friendId)) {
            log.debug("Users {} and {} are now friends",
                    findUserById(userId).getName(),
                    findUserById(friendId).getName());
            return findUserById(userId);
        } else {
            throw new BadArgumentsException("Users are already friends");
        }
    }

    public User deleteFriends(Integer userId, Integer friendId) {
        if (findUserById(userId).deleteFriend(friendId) &&
                findUserById(friendId).deleteFriend(userId)) {
            log.debug("Users {} and {} are no longer friends",
                    findUserById(userId).getName(),
                    findUserById(friendId).getName());
            return findUserById(userId);
        } else {
            throw new BadArgumentsException("Users are not friends");
        }
    }

    public Collection<User> findFriends(Integer userId) {
        log.debug("Listing {}'s friends", findUserById(userId).getName()
        );
        return findUserById(userId).getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(Integer userId, Integer otherId) throws NoSuchUserException {
        log.debug("Listing {}'s and {}'s common friends", findUserById(userId), findUserById(otherId));
        return findUserById(userId).getFriends().stream()
                .filter(id -> findUserById(otherId).getFriends().contains(id))
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public User findUserById(Integer userId) {
        return userStorage.findUserById(userId).orElseThrow(
                () -> new NoSuchUserException("No user with such ID: " + userId));
    }

    private Integer getNextId() {
        return ++userId;
    }
}
