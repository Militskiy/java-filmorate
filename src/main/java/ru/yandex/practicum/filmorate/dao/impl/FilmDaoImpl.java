package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component("FilmDaoImpl")
@Slf4j
public class FilmDaoImpl implements FilmDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        getFilmParameters(parameters, film);

        jdbcTemplate.update(CREATE_FILM, parameters, keyHolder, new String[]{"FILM_ID"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres().size() > 0) {
            StringBuilder sb = new StringBuilder();
            film.getGenres().forEach(genre -> {
                parameters
                        .addValue("filmId", id)
                        .addValue("genreId" + genre.getId(), genre.getId());
                sb.append(ADD_GENRE).append(genre.getId()).append(");\n");
            });
            jdbcTemplate.update(sb.toString(), parameters);
        }
        film.setMpa(getFilmRating(film.getId()));
        film.getGenres().clear();
        getFilmGenres(film.getId()).forEach(film::addGenre);
        return film;
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        getFilmParameters(parameters, film);
        if (jdbcTemplate.update(UPDATE_FILM_QUERY, parameters) == 1) {
            if (film.getGenres().size() > 0) {
                StringBuilder sb = new StringBuilder(DELETE_GENRES_QUERY);
                film.getGenres().forEach(genre -> {
                    parameters
                            .addValue("genreId" + genre.getId(), genre.getId());
                    sb.append(ADD_GENRE).append(genre.getId()).append(");\n");
                });
                try {
                    jdbcTemplate.update(sb.toString(), parameters);
                } catch (DataAccessException e) {
                    throw new NoSuchGenreException("No such genre");
                }
                film.getGenres().clear();
                getFilmGenres(film.getId()).forEach(film::addGenre);
            } else {
                jdbcTemplate.update(DELETE_GENRES_QUERY, parameters);
            }
            film.setMpa(getFilmRating(film.getId()));
            return film;
        } else {
            throw new NoSuchFilmException("No Film with such ID: " + film.getId());
        }
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate().query(FIND_FILM, (rs, rowNum) -> makeFilm(rs), filmId)
                .stream().peek(film -> {
                    getFilmLikes(film.getId()).forEach(film::addLike);
                    getFilmGenres(film.getId()).forEach(film::addGenre);
                }).findAny();
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
        try {
            jdbcTemplate.getJdbcTemplate().update(ADD_LIKE, userId, filmId);
        } catch (DataAccessException e) {
            throw new BadArgumentsException("No such users or already liked");
        }
    }

    @Override
    public int removeLike(Integer filmId, Integer userId) {
        try {
            return jdbcTemplate.getJdbcTemplate().update(DELETE_LIKE, filmId, userId);
        } catch (DataAccessException e) {
            throw new BadArgumentsException("No such users or already disliked");
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

    private Collection<User> getFilmLikes(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate()
                .query(GET_FILM_LIKES, (rs, rowNum) -> makeUser(rs), filmId);
    }

    private Collection<Genre> getFilmGenres(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate().query(GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Mpa getFilmRating(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate()
                .query(GET_FILM_RATING, (rs, rowNum) -> makeMpa(rs), filmId).stream().findFirst().orElse(null);
    }

    private void getFilmParameters(MapSqlParameterSource parameterSource, Film film) {
        parameterSource
                .addValue("filmName", film.getName())
                .addValue("filmDescription", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("ratingId", film.getMpa().getId());
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
        return new Film(id, filmName, filmDescription, releaseDate, duration, new Mpa(ratingId, ratingName));
    }
}
