package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component("UserDaoImpl")
@AllArgsConstructor
@Slf4j
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        Collection<User> result = jdbcTemplate.query(FIND_ALL_QUERY, (rs, rowNum) -> makeUser(rs));
        result.forEach(user -> getFriends(user.getId()).forEach(user::addFriend));
        return result;
    }

    @Override
    public Optional<User> findById(Integer id) {
        Optional<User> optionalUser =
                jdbcTemplate.query(FIND_USER_QUERY, (rs, rowNum) -> makeUser(rs), id).stream().findFirst();
        optionalUser.ifPresent(user -> getFriends(user.getId()).forEach(user::addFriend));
        return optionalUser;
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE_USER_QUERY, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        log.debug("Created user: {} with id: {}", user.getName(), id);
        return user;
    }

    @Override
    public int update(User user) {
        log.debug("Updating user: {} with id: {}", user.getName(), user.getId());
        return jdbcTemplate.update(UPDATE_USER_QUERY,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
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
        Collection<User> result = jdbcTemplate.query(FIND_FRIENDS_QUERY, (rs, rowNum) -> makeUser(rs), userId);
        result.forEach(user -> getFriends(user.getId()).forEach(user::addFriend));
        return result;
    }

    @Override
    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        Collection<User> result = jdbcTemplate.query(FIND_COMMON_FRIENDS_QUERY,
                (rs, rowNum) -> makeUser(rs), userId, otherId, userId, otherId);
        result.forEach(user -> getFriends(user.getId()).forEach(user::addFriend));
        return result;
    }

    // для заполнения друзей в методах findAll/findById/findFriends/findCommonFriends
    private Collection<User> getFriends(Integer userId) {
        return jdbcTemplate.query(FIND_FRIENDS_QUERY, (rs, rowNum) -> makeUser(rs), userId);
    }
}
