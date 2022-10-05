package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Component
@Slf4j
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> findById(Integer genreId) {
        return jdbcTemplate.query(FIND_GENRE_BY_ID, (rs, rowNum) -> makeGenre(rs), genreId).stream().findFirst();
    }

    @Override
    public Collection<Genre> findAll() {
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> makeGenre(rs));
    }
}
