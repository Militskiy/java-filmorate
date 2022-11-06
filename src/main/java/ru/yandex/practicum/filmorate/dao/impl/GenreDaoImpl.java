package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@AllArgsConstructor
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(Integer genreId) {
        return jdbcTemplate.query(FIND_GENRE_BY_ID, (rs, rowNum) -> makeGenre(rs), genreId)
                .stream()
                .findAny()
                .orElseThrow((() -> new NoSuchGenreException("There is no genre with ID: " + genreId)));
    }

    @Override
    public Collection<Genre> findAll() {
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> makeGenre(rs));
    }
}
