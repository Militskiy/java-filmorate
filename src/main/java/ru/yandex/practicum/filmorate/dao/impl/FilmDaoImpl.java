package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenreException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component("FilmDaoImpl")
@Slf4j
public class FilmDaoImpl implements FilmDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserDaoImpl userStorage;

    @Override
    public Film create(Film film) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        getFilmParameters(parameters, film);

        jdbcTemplate.update(CREATE_FILM, parameters, keyHolder, new String[]{"FILM_ID"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres().size() > 0) {
            try {
                filmGenreUpdate(film);
            } catch (DataAccessException e) {
                throw new NoSuchGenreException("No such genre");
            }
        }
        return findById(id);
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        getFilmParameters(parameters, film);
        if (jdbcTemplate.update(UPDATE_FILM_QUERY, parameters) == 1) {
            if (film.getGenres().size() > 0) {
                try {
                    jdbcTemplate.update(DELETE_GENRES_QUERY, parameters);
                    filmGenreUpdate(film);
                } catch (DataAccessException e) {
                    throw new NoSuchGenreException("No such genre");
                }
            } else {
                jdbcTemplate.update(DELETE_GENRES_QUERY, parameters);
            }
            return findById(film.getId());
        } else {
            throw new NoSuchFilmException("No Film with such ID: " + film.getId());
        }
    }

    @Override
    public Film findById(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate().query(FIND_FILM, (rs, rowNum) -> makeFilm(rs), filmId)
                .stream().peek(film -> {
                    getFilmLikes(film.getId()).forEach(film::addLike);
                    getFilmGenres(film.getId()).forEach(film::addGenre);
                }).findAny().orElseThrow(() -> new NoSuchFilmException("No Film with such ID: " + filmId));
    }

    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.getJdbcTemplate().query(FIND_ALL, (rs, rowNum) -> makeFilm(rs))
                .stream().peek(film -> {
                    getFilmLikes(film.getId()).forEach(film::addLike);
                    getFilmGenres(film.getId()).forEach(film::addGenre);
                }).collect(Collectors.toList());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        assert userStorage.findById(userId) != null;
        if (findById(filmId).getUserLikes()
                .stream()
                .noneMatch(user -> user.getId() == userId)) {
            jdbcTemplate.getJdbcTemplate().update(ADD_LIKE, userId, filmId);
        } else {
            throw new BadArgumentsException("Film already liked");
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        assert userStorage.findById(userId) != null;
        if (findById(filmId).getUserLikes()
                .stream()
                .anyMatch(user -> user.getId() == userId)) {
            jdbcTemplate.getJdbcTemplate().update(DELETE_LIKE, filmId, userId);
        } else {
            throw new NoSuchUserException("Film not liked");
        }
    }

    @Override
    public Collection<Film> findPopularFilms(Integer count) {
        return jdbcTemplate.getJdbcTemplate().query(FIND_POPULAR_FILMS, (rs, rowNum) -> makeFilm(rs), count)
                .stream().peek(film -> {
                    getFilmLikes(film.getId()).forEach(film::addLike);
                    getFilmGenres(film.getId()).forEach(film::addGenre);
                }).collect(Collectors.toList());
    }

    @Override
    public List<Film> findDirectorFilms(int directorId, String sortBy) {
        String sqlQuery = sortBy.equals("likes")?GET_DIRECTOR_FILMS_LIKES_SORTED:GET_DIRECTOR_FILMS_YEAR_SORTED;
        return jdbcTemplate.getJdbcTemplate().query(sqlQuery, (rs, rowNum) -> makeFilm(rs), directorId);
    }

    private Collection<User> getFilmLikes(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate()
                .query(GET_FILM_LIKES, (rs, rowNum) -> makeUser(rs), filmId);
    }

    private Collection<Genre> getFilmGenres(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate().query(GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private void getFilmParameters(MapSqlParameterSource parameterSource, Film film) {
        parameterSource
                .addValue("filmName", film.getName())
                .addValue("filmDescription", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("ratingId", film.getMpa().getId())
                .addValue("directorId", film.getDirector().getId());
        if (film.getId() > 0) {
            parameterSource.addValue("filmId", film.getId());
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String filmName = rs.getString("film_name");
        String filmDescription = rs.getString("film_description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("rating_id");
        String ratingName = rs.getString("rating_name");
        int directorId = rs.getInt("director_id");
        String directorName = rs.getString("director_name");
        return new Film(id, filmName, filmDescription, releaseDate, duration, new Mpa(ratingId, ratingName),
                new Director(directorId, directorName));
    }

    private void filmGenreUpdate(Film film) {
        List<Genre> filmGenres = new ArrayList<>(film.getGenres());
        jdbcTemplate.getJdbcTemplate().batchUpdate(FILM_GENRE_UPDATE,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, filmGenres.get(i).getId());
                    }
                    @Override
                    public int getBatchSize() {
                        return filmGenres.size();
                    }
                });
    }
}
