package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@AllArgsConstructor
@Component
@Slf4j
public class DbFriendStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Friend> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, (rs, rowNum) -> makeFriend(rs));
    }

    @Override
    public void createFriend(Integer userId, Integer friendId) {
        try {
            jdbcTemplate.update(CREATE_FRIEND_QUERY,
                    userId, friendId,
                    userId, friendId,
                    friendId, userId, userId, friendId,
                    friendId, userId, userId, friendId);
        } catch (DataAccessException e) {
            throw new NoSuchUserException("No such users");
        }
    }

    @Override
    public boolean delete(Integer userId, Integer friendId) {
        return jdbcTemplate.update(DELETE_FRIEND_QUERY, userId, friendId) > 0;
    }

    @Override
    public Collection<User> findFriends(Integer userId) {
        return jdbcTemplate.query(FIND_FRIENDS_QUERY, (rs, rowNum) -> makeUser(rs), userId, userId);
    }

    @Override
    public Collection<User> findCommonFriends(Integer userId, Integer otherId) {
        return jdbcTemplate.query(FIND_COMMON_FRIENDS_QUERY,
                (rs, rowNum) -> makeUser(rs), userId, otherId, userId, otherId);
    }

    private Friend makeFriend(ResultSet rs) throws SQLException {
        int id = rs.getInt("link_id");
        int friend = rs.getInt("first_user_id");
        boolean confirmed = rs.getBoolean("confirmed");
        return new Friend(id, friend, confirmed);
    }
}
