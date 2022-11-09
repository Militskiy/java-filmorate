package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("UserDaoImpl")
    private final UserDao userStorage;
    private final FilmDao filmStorage;

    public Collection<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public void removeUser(Integer userId) {
        userStorage.removeUser(userId);
    }

    public void addFriends(Integer userId, Integer friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> findFriends(Integer userId) {
        return userStorage.findFriends(userId);
    }

    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        return userStorage.findCommonFriends(userId, otherId);
    }

    public Collection<Film> getRecommendations(Integer userId) {
        return filmStorage.getRecommendations(userId);
    }

    public User findUserById(Integer userId) {
        return userStorage.findById(userId);
    }

    public Collection<Event> findFeed(Integer id) {
        return userStorage.findFeed(id);
    }
}
