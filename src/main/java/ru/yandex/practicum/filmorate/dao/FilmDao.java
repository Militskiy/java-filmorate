package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmDao extends Dao<Film> {

    String CREATE_FILM = "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, DIRECTOR_ID) " +
            "VALUES (:filmName, :filmDescription, :releaseDate, :duration, :ratingId, :directorId);";

    String FILM_GENRE_UPDATE = "INSERT INTO GENRES_FILMS (FILM_ID, GENRE_ID) VALUES (?, ?)";

    String DELETE_GENRES_QUERY = "DELETE FROM GENRES_FILMS WHERE FILM_ID = :filmId;\n";

    String UPDATE_FILM_QUERY =
            "UPDATE FILMS\n" +
                    "SET FILM_NAME = :filmName, " +
                    "FILM_DESCRIPTION = :filmDescription, " +
                    "RELEASE_DATE = :releaseDate, " +
                    "DURATION = :duration, " +
                    "RATING_ID = :ratingId, " +
                    "DIRECTOR_ID = :directorId\n" +
                    "WHERE FILM_ID = :filmId";

    String FIND_FILM =
            "SELECT FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       R.RATING_ID AS RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       D.DIRECTOR_ID AS DIRECTOR_ID,\n" +
                    "       DIRECTOR_NAME\n" +
                    "FROM FILMS\n" +
                    "         LEFT OUTER JOIN RATINGS R on R.RATING_ID = FILMS.RATING_ID\n"  +
                    "         LEFT OUTER JOIN DIRECTORS D on D.DIRECTOR_ID = FILMS.DIRECTOR_ID\n" +
                    "WHERE FILM_ID = ?";
    String FIND_ALL =
            "SELECT FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       R.RATING_ID AS RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       D.DIRECTOR_ID AS DIRECTOR_ID,\n" +
                    "       DIRECTOR_NAME\n" +
                    "FROM FILMS\n" +
                    "         LEFT OUTER JOIN RATINGS R on R.RATING_ID = FILMS.RATING_ID\n" +
                    "         LEFT OUTER JOIN DIRECTORS D on D.DIRECTOR_ID = FILMS.DIRECTOR_ID\n";

    String ADD_LIKE =
            "INSERT INTO LIKES (USER_ID, FILM_ID)\n" +
                    "VALUES (?, ?)";

    String DELETE_LIKE = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";

    String GET_FILM_LIKES = "SELECT * FROM USERS WHERE USER_ID IN (SELECT USER_ID FROM LIKES WHERE FILM_ID = ?)";
    String GET_FILM_GENRES =
            "SELECT * " +
                    "FROM GENRES " +
                    "WHERE GENRE_ID IN " +
                    "(SELECT GENRE_ID " +
                    "FROM GENRES_FILMS " +
                    "WHERE FILM_ID = ?)";

    String FIND_POPULAR_FILMS =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       F.DIRECTOR_ID,\n" +
                    "       DIRECTOR_NAME\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = F.DIRECTOR_ID\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY COUNT(L.FILM_ID) DESC\n" +
                    "LIMIT ?;";

    String GET_DIRECTOR_FILMS_YEAR_SORTED =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       F.DIRECTOR_ID,\n" +
                    "       DIRECTOR_NAME\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = F.DIRECTOR_ID\n" +
                    "WHERE F.DIRECTOR_ID = ?\n" +
                    "ORDER BY RELEASE_DATE DESC\n";

    String GET_DIRECTOR_FILMS_LIKES_SORTED =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       F.DIRECTOR_ID,\n" +
                    "       DIRECTOR_NAME\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = F.DIRECTOR_ID\n" +
                    "WHERE F.DIRECTOR_ID = ?\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY COUNT(L.FILM_ID) DESC\n";

    Film create(Film film);

    Film update(Film film);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Collection<Film> findPopularFilms(Integer count);

    List<Film> findDirectorFilms(int directorId, String sortBy);
}
