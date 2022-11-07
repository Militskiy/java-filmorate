package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmDao extends Dao<Film> {

    String CREATE_FILM = "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
            "VALUES (:filmName, :filmDescription, :releaseDate, :duration, :ratingId);";

    String FILM_GENRE_UPDATE = "INSERT INTO GENRES_FILMS (FILM_ID, GENRE_ID) VALUES (?, ?)";

    String FILM_DIRECTOR_UPDATE = "INSERT INTO DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";

    String DELETE_GENRES_QUERY = "DELETE FROM GENRES_FILMS WHERE FILM_ID = :filmId;\n";

    String DELETE_DIRECTORS_QUERY = "DELETE FROM DIRECTORS_FILMS WHERE FILM_ID = :filmId;\n";

    String UPDATE_FILM_QUERY =
            "UPDATE FILMS\n" +
                    "SET FILM_NAME = :filmName, " +
                    "FILM_DESCRIPTION = :filmDescription, " +
                    "RELEASE_DATE = :releaseDate, " +
                    "DURATION = :duration, " +
                    "RATING_ID = :ratingId\n" +
                    "WHERE FILM_ID = :filmId";

    String FIND_FILM =
            "SELECT FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       R.RATING_ID AS RATING_ID,\n" +
                    "       RATING_NAME\n" +
                    "FROM FILMS\n" +
                    "         LEFT OUTER JOIN RATINGS R on R.RATING_ID = FILMS.RATING_ID\n" +
                    "WHERE FILM_ID = ?";
    String FIND_ALL =
            "SELECT FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       R.RATING_ID AS RATING_ID,\n" +
                    "       RATING_NAME\n" +
                    "FROM FILMS\n" +
                    "         LEFT OUTER JOIN RATINGS R on R.RATING_ID = FILMS.RATING_ID\n";

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

    String GET_FILM_DIRECTORS =
            "SELECT * " +
                    "FROM DIRECTORS " +
                    "WHERE DIRECTOR_ID IN " +
                    "(SELECT DIRECTOR_ID " +
                    "FROM DIRECTORS_FILMS " +
                    "WHERE FILM_ID = ?)";

    String FIND_POPULAR_FILMS =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY COUNT(L.FILM_ID) DESC\n" +
                    "LIMIT ?;";

    String DELETE_FILM = "DELETE FROM FILMS WHERE FILM_ID = ?";

    String RECOMMENDED_FILMS =
            "SELECT F.FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, F.RATING_ID, RATING_NAME\n" +
                    "FROM LIKES AS L\n" +
                    "         JOIN FILMS AS F ON F.FILM_ID = L.FILM_ID\n" +
                    "         JOIN RATINGS AS R ON R.RATING_ID = F.RATING_ID\n" +
                    "WHERE L.FILM_ID NOT IN (SELECT FILM_ID FROM LIKES WHERE USER_ID = ?)\n" +
                    "  AND L.USER_ID IN (SELECT USER_ID\n" +
                    "                    FROM LIKES\n" +
                    "                    WHERE FILM_ID IN (SELECT FILM_ID FROM LIKES WHERE USER_ID = ?)\n" +
                    "                      AND USER_ID != ?\n" +
                    "                    GROUP BY USER_ID\n" +
                    "                    ORDER BY COUNT(FILM_ID) DESC\n" +
                    "                    LIMIT 1);";

    String GET_DIRECTOR_FILMS_YEAR_SORTED =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME\n" +
                    "FROM DIRECTORS_FILMS DF\n" +
                    "LEFT JOIN FILMS F on F.FILM_ID = DF.FILM_ID\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
                    "WHERE DF.DIRECTOR_ID = ?\n" +
                    "ORDER BY F.RELEASE_DATE ASC\n";

    String GET_DIRECTOR_FILMS_LIKES_SORTED =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME\n" +
                    "FROM DIRECTORS_FILMS DF\n" +
                    "LEFT JOIN FILMS F on F.FILM_ID = DF.FILM_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
                    "WHERE DF.DIRECTOR_ID = ?\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY COUNT(L.FILM_ID) DESC\n";

    String SEARCH_BY_DIRECTOR =
            "SELECT F.FILM_ID,\n" +
                    "       F.FILM_NAME,\n" +
                    "       F.FILM_DESCRIPTION,\n" +
                    "       F.RELEASE_DATE,\n" +
                    "       F.DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       R.RATING_NAME\n" +
            "FROM FILMS F\n" +
            "JOIN DIRECTORS_FILMS DF ON F.FILM_ID = DF.FILM_ID\n" +
            "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
            "LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
            "WHERE UPPER(D.DIRECTOR_NAME) LIKE UPPER\n";

    String SEARCH_BY_FILM_NAME =
            "SELECT F.FILM_ID,\n" +
                    "       F.FILM_NAME,\n" +
                    "       F.FILM_DESCRIPTION,\n" +
                    "       F.RELEASE_DATE,\n" +
                    "       F.DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       R.RATING_NAME\n" +
            "FROM FILMS F\n" +
            "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
            "WHERE UPPER(F.FILM_NAME) LIKE UPPER\n";

    /*String SEARCH_BY_DIRECTOR =
            "SELECT F.FILM_ID\n" +
            "FROM FILMS F\n" +
            "JOIN DIRECTORS_FILMS DF ON F.FILM_ID = DF.FILM_ID\n" +
            "LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
            "WHERE UPPER(D.DIRECTOR_NAME) LIKE UPPER\n";

    String SEARCH_BY_FILM_NAME =
            "SELECT F.FILM_ID,\n" +
            "FROM FILMS F\n" +
            "WHERE UPPER(F.FILM_NAME) LIKE UPPER\n";*/

    Film create(Film film);

    Film update(Film film);

    void removeFilm(Integer filmId);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Collection<Film> findPopularFilms(Integer count);

    List<Film> getRecommendations(Integer userId);

    List<Film> findDirectorFilms(int directorId, String sortBy);

    List<Film> search(String query, List<String> searchFilters);
}
