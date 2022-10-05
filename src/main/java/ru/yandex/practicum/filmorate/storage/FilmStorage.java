package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage extends Storage<Film> {

    String CREATE_FILM = "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
            "VALUES (:filmName, :filmDescription, :releaseDate, :duration, :ratingId);";

    String ADD_GENRE = "INSERT INTO GENRES_FILMS (FILM_ID, GENRE_ID) " +
            "VALUES (:filmId, :genreId";
    String DELETE_GENRES_QUERY = "DELETE FROM GENRES_FILMS WHERE FILM_ID = :filmId;\n";

    String UPDATE_FILM_QUERY = "UPDATE FILMS\n" +
            "SET FILM_NAME = :filmName, FILM_DESCRIPTION = :filmDescription, RELEASE_DATE = :releaseDate, DURATION = :duration, RATING_ID = :ratingId\n" +
            "WHERE FILM_ID = :filmId";

    String FIND_FILM = "SELECT * FROM FILMS WHERE FILM_ID = ?";
    String FIND_ALL = "SELECT * FROM FILMS";

    String ADD_LIKE =
            "INSERT INTO LIKES (USER_ID, FILM_ID)\n" +
                    "VALUES (?, ?)";

    String GET_FILM_LIKES = "SELECT USER_ID FROM LIKES WHERE FILM_ID = ?";
    String GET_FILM_GENRES =
            "SELECT * " +
                    "FROM GENRES " +
                    "WHERE GENRE_ID IN " +
                    "(SELECT GENRE_ID " +
                    "FROM GENRES_FILMS " +
                    "WHERE FILM_ID = ?)";
    String GET_FILM_RATING =
            "SELECT * " +
                    "FROM RATINGS " +
                    "WHERE RATING_ID IN " +
                    "(SELECT RATING_ID " +
                    "FROM FILMS WHERE " +
                    "FILM_ID = ?)";

    Film create(Film film);

    Film update(Film film);

    Film findById(Integer filmId);

    void addLike(Integer filmId, Integer userId);
    void removeLike(Integer filmId, Integer userId);
}
