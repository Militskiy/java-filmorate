package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchDirectorException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Component("FilmDaoImpl")
public class FilmDaoImpl implements FilmDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserDao userStorage;
    private final DirectorDAO directorStorage;
    private final EventDao eventStorage;
    private final GenreDao genreStorage;

    @Override
    public Film create(Film film) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        getFilmParameters(parameters, film);
        film.getDirectors().forEach(director -> directorStorage.findById(director.getId()).orElseThrow(
                () -> new NoSuchDirectorException("No such director")
        ));
        film.getGenres().forEach(genre -> genreStorage.findById(genre.getId()));
        jdbcTemplate.update(CREATE_FILM, parameters, keyHolder, new String[]{"FILM_ID"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres().size() > 0) {
            try {
                filmGenreUpdate(film);
            } catch (DataIntegrityViolationException e) {
                throw new NoSuchGenreException("No such genre");
            }
        }
        if (film.getDirectors().size() > 0) {
            try {
                filmDirectorUpdate(film);
            } catch (DataIntegrityViolationException e) {
                throw new NoSuchDirectorException("No such director");
            }
        }
        return findById(id);
    }

    @Override
    public Film update(Film film) {
        findById(film.getId());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        getFilmParameters(parameters, film);
        film.getDirectors().forEach(director -> directorStorage.findById(director.getId()).orElseThrow(
                () -> new NoSuchDirectorException("No such director")
        ));
        film.getGenres().forEach(genre -> genreStorage.findById(genre.getId()));
        if (jdbcTemplate.update(UPDATE_FILM_QUERY, parameters) == 1) {
            if (film.getGenres().size() > 0) {
                try {
                    jdbcTemplate.update(DELETE_GENRES_QUERY, parameters);
                    filmGenreUpdate(film);
                } catch (DataIntegrityViolationException e) {
                    throw new NoSuchGenreException("No such genre");
                }
            } else {
                jdbcTemplate.update(DELETE_GENRES_QUERY, parameters);
            }
            if (film.getDirectors().size() > 0) {
                try {
                    jdbcTemplate.update(DELETE_DIRECTORS_QUERY, parameters);
                    filmDirectorUpdate(film);
                } catch (DataIntegrityViolationException e) {
                    throw new NoSuchDirectorException("No such director");
                }
            } else {
                jdbcTemplate.update(DELETE_DIRECTORS_QUERY, parameters);
            }
            return findById(film.getId());
        } else {
            throw new NoSuchFilmException("No Film with such ID: " + film.getId());
        }
    }

    @Override
    public Film findById(Integer filmId) {
        return addFilmFields(jdbcTemplate.getJdbcTemplate().query(FIND_FILM, (rs, rowNum) -> makeFilm(rs), filmId))
                .stream()
                .findAny().orElseThrow(() -> new NoSuchFilmException("No Film with such ID: " + filmId));
    }

    @Override
    public Collection<Film> findAll() {
        return addFilmFields(jdbcTemplate.getJdbcTemplate().query(FIND_ALL, (rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public void removeFilm(Integer filmId) {
        int result = jdbcTemplate.getJdbcTemplate().update(DELETE_FILM, filmId);
        if (result != 1) {
            throw new NoSuchFilmException("Film was not found");
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId, Integer rate) {
        userStorage.findById(userId);
        if (findById(filmId).getUserLikes()
                .stream()
                .noneMatch(user -> user.getId() == userId)) {
            jdbcTemplate.getJdbcTemplate().update(ADD_LIKE, userId, filmId, rate);
            jdbcTemplate.getJdbcTemplate().update(UPDATE_FILM_RATE, filmId, filmId);
            eventStorage.createEvent(userId, EventType.LIKE, Operation.ADD, filmId);
        } else {
            throw new BadArgumentsException("Film already liked");
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        userStorage.findById(userId);
        if (findById(filmId).getUserLikes()
                .stream()
                .anyMatch(user -> user.getId() == userId)) {
            jdbcTemplate.getJdbcTemplate().update(DELETE_LIKE, filmId, userId);
            jdbcTemplate.getJdbcTemplate().update(UPDATE_FILM_RATE, filmId, filmId);
            eventStorage.createEvent(userId, EventType.LIKE, Operation.REMOVE, filmId);
        } else {
            throw new BadArgumentsException("Film not liked");
        }
    }

    @Override
    public Collection<Film> findPopularFilms(Integer count) {
        return addFilmFields(
                jdbcTemplate.getJdbcTemplate().query(FIND_POPULAR_FILMS, (rs, rowNum) -> makeFilm(rs), count));
    }

    @Override
    public Collection<Film> findCommonFilms(Integer userId, Integer friendId) {
        return addFilmFields(
                jdbcTemplate.getJdbcTemplate()
                        .query(FIND_COMMON_FILMS_COUPLE_FRIENDS, (rs, rowNum) -> makeFilm(rs), userId, friendId)
        );
    }

    @Override
    public Collection<Film> findDirectorFilms(int directorId, String sortBy) {
        directorStorage.findById(directorId)
                .orElseThrow(() -> new NoSuchDirectorException(String.format("Director with id = %d not found", directorId)));

        String sqlQuery = sortBy.equals("likes") ? GET_DIRECTOR_FILMS_LIKES_SORTED : GET_DIRECTOR_FILMS_YEAR_SORTED;

        try (Stream<Film> stream
                     = jdbcTemplate.getJdbcTemplate()
                .queryForStream(sqlQuery, (rs, rowNum) -> makeFilm(rs), directorId)) {
            return addFilmFields(stream.collect(Collectors.toList()));
        }
    }

    @Override
    public Collection<Film> getTheMostPopularFilmsWithFilter(
            int count, Optional<Integer> genreId, Optional<Integer> year
    ) {
        return addFilmFields(jdbcTemplate.getJdbcTemplate()
                .query(GET_THE_MOST_POPULAR_FILMS_WITH_FILTERS, (rs, rowNum) -> makeFilm(rs),
                        year.orElse(0), year.isPresent(), genreId.orElse(0), genreId.isPresent(), count));
    }

    @Override
    public Collection<Film> search(String query, List<String> searchFilters) {
        StringBuilder sb = new StringBuilder();
        String searchByDirector = SEARCH_BY_DIRECTOR.replace("chars", query);
        String searchByFilmName = SEARCH_BY_FILM_NAME.replace("chars", query);

        for (int i = 0; i < searchFilters.size(); i++) {
            String s = searchFilters.get(i);
            if (s.equals(DIRECTOR)) sb.append(searchByDirector);
            if (s.equals(TITLE)) sb.append(searchByFilmName);
            if (!(i == searchFilters.size() - 1)) sb.append(UNION);
        }

        String sqlQuery = SQL_QUERY.replace("string", sb);
        return addFilmFields(jdbcTemplate.getJdbcTemplate().query(sqlQuery, (rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public Map<User, Map<Film, Double>> getRateData() {
        return jdbcTemplate.query(GET_LIKES, this::fillRateData)
                .stream()
                .collect(Collectors.groupingBy(Triple::getLeft, Collectors.toMap(Triple::getMiddle, Triple::getRight)));
    }

    private Collection<User> getFilmLikes(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate()
                .query(GET_FILM_LIKES, (rs, rowNum) -> makeUser(rs), filmId);
    }

    private Collection<Genre> getFilmGenres(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate().query(GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Collection<Director> getFilmDirectors(Integer filmId) {
        return jdbcTemplate.getJdbcTemplate().query(GET_FILM_DIRECTORS, (rs, rowNum) -> makeDirector(rs), filmId);
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
        double rate = rs.getDouble("film_rate");
        return new Film(id, filmName, filmDescription, releaseDate, duration, new Mpa(ratingId, ratingName), rate);
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


    private void filmDirectorUpdate(Film film) {
        List<Director> directors = new ArrayList<>(film.getDirectors());
        jdbcTemplate.getJdbcTemplate().batchUpdate(FILM_DIRECTOR_UPDATE,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, directors.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return directors.size();
                    }
                });
    }


    private Collection<Film> addFilmFields(Collection<Film> films) {
        return films
                .stream()
                .peek(film -> {
                    getFilmLikes(film.getId()).forEach(film::addLike);
                    getFilmGenres(film.getId()).forEach(film::addGenre);
                    getFilmDirectors(film.getId()).forEach(film::addDirector);
                }).collect(Collectors.toList());
    }

    private Triple<User, Film, Double> fillRateData(ResultSet rs, int rowNum) throws SQLException {
        User user = userStorage.findById(rs.getInt("user_id"));
        Film film = findById(rs.getInt("film_id"));
        double rate = rs.getDouble("like_rate");
        return Triple.of(user, film, rate);
    }
}
