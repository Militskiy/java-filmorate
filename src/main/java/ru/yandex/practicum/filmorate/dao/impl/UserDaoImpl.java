package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("UserDaoImpl")
@AllArgsConstructor
@Slf4j
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, (rs, rowNum) -> makeUser(rs))
                .stream()
                .peek(user -> getFriends(user.getId()).forEach(user::addFriend))
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Integer id) {
        return getOptionalUser(id).orElseThrow(() -> new NoSuchUserException("No user with such ID: " + id));
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("user_id");
        int id = simpleJdbcInsert.executeAndReturnKey(user.getMap()).intValue();
        log.debug("Created user: {} with id: {}", user.getName(), id);
        return findById(id);
    }

    @Override
    public User update(User user) {
        log.debug("Updating user: {} with id: {}", user.getName(), user.getId());
        if (jdbcTemplate.update(UPDATE_USER_QUERY,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId()) > 0) {
            return findById(user.getId());
        } else {
            throw new NoSuchUserException("No user with such ID: " + user.getId());
        }
    }

    @Override
    public void removeUser(Integer userId) {
        int result = jdbcTemplate.update(DELETE_USER, userId);
        if (result != 1) {
            throw new NoSuchUserException("User was not found");
        }
    }

    @Override
    public boolean createFriend(Integer userId, Integer friendId) {
        try {
            return jdbcTemplate.update(CREATE_FRIEND_QUERY,
                    userId, friendId,
                    userId, friendId,
                    friendId, userId, userId, friendId,
                    friendId, userId, userId, friendId) > 0;
        } catch (DataAccessException e) {
            throw new NoSuchUserException("No such users or attempting to add self");
        }
    }

    @Override
    public boolean delete(Integer userId, Integer friendId) {
        return jdbcTemplate.update(DELETE_FRIEND_QUERY, userId, friendId) > 0;
    }

    @Override
    public Collection<User> findFriends(Integer userId) {
        if (getOptionalUser(userId).isPresent()) {
            return jdbcTemplate.query(FIND_FRIENDS_QUERY, (rs, rowNum) -> makeUser(rs), userId)
                    .stream()
                    .peek(user -> getFriends(user.getId()).forEach(user::addFriend))
                    .collect(Collectors.toList());
        } else {
            throw new NoSuchUserException("No user with such ID: " + userId);
        }
    }

    @Override
    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        if (getOptionalUser(userId).isPresent()) {
            if (getOptionalUser(otherId).isPresent()) {
                return jdbcTemplate.query(FIND_COMMON_FRIENDS_QUERY,
                                (rs, rowNum) -> makeUser(rs), userId, otherId)
                        .stream()
                        .peek(user -> getFriends(user.getId()).forEach(user::addFriend))
                        .collect(Collectors.toList());
            } else {
                throw new NoSuchUserException("No user with such ID: " + otherId);
            }
        } else {
            throw new NoSuchUserException("No user with such ID: " + userId);
        }
    }

    // для заполнения друзей в методах findAll/findById/findFriends/findCommonFriends
    private Collection<User> getFriends(Integer userId) {
        return jdbcTemplate.query(FIND_FRIENDS_QUERY, (rs, rowNum) -> makeUser(rs), userId);
    }

    private Optional<User> getOptionalUser(Integer id) {
        return jdbcTemplate.query(FIND_USER_QUERY, (rs, rowNum) -> makeUser(rs), id)
                .stream()
                .peek(user -> getFriends(user.getId()).forEach(user::addFriend))
                .findAny();
    }
}
