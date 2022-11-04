package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.exceptions.NoSuchDirectorException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class DirectorImpl implements DirectorDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> findAll() {
        try (Stream<Director> stream = jdbcTemplate.queryForStream(FIND_ALL_QUERY, this::mapRowToDirector)) {
            return stream.collect(Collectors.toList());
        }
    }

    @Override
    public Optional<Director> findById(int id) {
        try (Stream<Director> stream = jdbcTemplate.queryForStream(FIND_BY_ID_QUERY, this::mapRowToDirector, id)) {
            return stream.findFirst();
        }
    }

    @Override
    public Director create(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY, new String[]{"DIRECTOR_ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findById(id).orElseThrow(() -> new NoSuchDirectorException(String.format("Director with id = %d not found", id)));
    }

    @Override
    public Director update(Director director) {
        int id = director.getId();

        jdbcTemplate.update(UPDATE_QUERY,
                director.getName(),
                id);
        return findById(id).orElseThrow(() -> new NoSuchDirectorException(String.format("Director with id = %d not found", id)));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    private Director mapRowToDirector(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("DIRECTOR_ID");
        String name = resultSet.getString("DIRECTOR_NAME");
        return new Director(id, name);
    }
}
