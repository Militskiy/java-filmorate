package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

public interface Storage<T> {
    Collection<T> findAll();

    default User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("user_email");
        String login = rs.getString("user_login");
        String name = rs.getString("user_name");
        LocalDate birthday = Objects.requireNonNull(rs.getDate("birthday")).toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    default Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");
        return new Genre(id, name);
    }

    default Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("rating_id");
        String name = rs.getString("rating_name");
        return new Mpa(id, name);
    }
}
