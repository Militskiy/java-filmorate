package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
                    "       RATING_NAME,\n" +
                    "       FILM_RATE\n" +
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
                    "       RATING_NAME,\n" +
                    "       FILM_RATE\n" +
                    "FROM FILMS\n" +
                    "         LEFT OUTER JOIN RATINGS R on R.RATING_ID = FILMS.RATING_ID\n";

    String ADD_LIKE =
            "INSERT INTO LIKES (USER_ID, FILM_ID, LIKE_RATE)\n" +
                    "VALUES (?, ?, ?)";

    String UPDATE_FILM_RATE =
            "UPDATE FILMS\n" +
                    "SET FILM_RATE = (SELECT COALESCE(ROUND(AVG(LIKE_RATE), 1), 4) as AVERAGE_RATE\n" +
                    "                 FROM LIKES\n" +
                    "                 WHERE FILM_ID = ?)\n" +
                    "WHERE FILM_ID = ?;";

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
                    "       RATING_NAME,\n" +
                    "       FILM_RATE\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY FILM_RATE DESC, COUNT(L.FILM_ID) DESC\n" +
                    "LIMIT ?;";

    String FIND_COMMON_FILMS_COUPLE_FRIENDS =
                    "SELECT F.*,\n" +
                    "R.RATING_NAME\n" +
                    "FROM FILMS F\n" +
                    "         JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "         LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "WHERE F.FILM_ID IN (SELECT FILM_ID\n" +
                    "                    FROM LIKES\n" +
                    "                    WHERE USER_ID = ?\n" +
                    "                       OR USER_ID = ?\n" +
                    "                    GROUP BY FILM_ID\n" +
                    "                    HAVING COUNT(FILM_ID) > 1)\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY F.FILM_RATE DESC, COUNT(L.FILM_ID) DESC;";


    String DELETE_FILM = "DELETE FROM FILMS WHERE FILM_ID = ?";

    String RECOMMENDED_FILMS =
            "SELECT F.FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, F.RATING_ID, RATING_NAME, FILM_RATE\n" +
                    "FROM LIKES AS L\n" +
                    "         JOIN FILMS AS F ON F.FILM_ID = L.FILM_ID\n" +
                    "         JOIN RATINGS AS R ON R.RATING_ID = F.RATING_ID\n" +
                    "WHERE L.FILM_ID NOT IN (SELECT FILM_ID FROM LIKES WHERE USER_ID = ?)\n" +
                    "  AND L.USER_ID IN (SELECT USER_ID\n" +
                    "                    FROM LIKES\n" +
                    "                    WHERE FILM_ID IN (SELECT FILM_ID FROM LIKES WHERE USER_ID = ?)\n" +
                    "                      AND USER_ID != ?\n" +
                    "                    GROUP BY USER_ID\n" +
                    "                    ORDER BY FILM_RATE DESC, COUNT(FILM_ID) DESC\n" +
                    "                    LIMIT 1);";

    String GET_DIRECTOR_FILMS_YEAR_SORTED =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       F.FILM_RATE\n" +
                    "FROM DIRECTORS_FILMS DF\n" +
                    "LEFT JOIN FILMS F on F.FILM_ID = DF.FILM_ID\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
                    "WHERE DF.DIRECTOR_ID = ?\n" +
                    "ORDER BY F.RELEASE_DATE ASC, F.FILM_RATE DESC\n";

    String GET_DIRECTOR_FILMS_LIKES_SORTED =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       F.FILM_RATE\n" +
                    "FROM DIRECTORS_FILMS DF\n" +
                    "LEFT JOIN FILMS F on F.FILM_ID = DF.FILM_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
                    "WHERE DF.DIRECTOR_ID = ?\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY FILM_RATE DESC, COUNT(L.FILM_ID) DESC\n";

    String GET_THE_MOST_POPULAR_FILMS_WITH_FILTERS =
            "SELECT DISTINCT F.*, LF.QL, RATING_NAME\n" +
                    "FROM FILMS AS F\n" +
                    "         LEFT JOIN (SELECT L.FILM_ID, COUNT(DISTINCT L.USER_ID) AS QL FROM LIKES AS L GROUP BY L.FILM_ID)\n" +
                    "    AS LF on F.FILM_ID = LF.FILM_ID\n" +
                    "         INNER JOIN GENRES_FILMS GF on f.FILM_ID = GF.FILM_ID\n" +
                    "INNER JOIN RATINGS R on R.RATING_ID = f.RATING_ID\n" +
                    "WHERE ((YEAR(F.RELEASE_DATE) = ?) or ?=false)\n" +
                    "  AND ((GF.GENRE_ID = ?) or ? = false)\n" +
                    "ORDER BY F.FILM_RATE DESC, QL DESC\n" +
                    "LIMIT ?;";


    String SEARCH_BY_DIRECTOR =
            "SELECT F.*,\n" +
                    "       R.RATING_NAME\n" +
                    "FROM FILMS F\n" +
                    "JOIN DIRECTORS_FILMS DF ON F.FILM_ID = DF.FILM_ID\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = DF.DIRECTOR_ID\n" +
                    "WHERE UPPER(D.DIRECTOR_NAME) LIKE UPPER\n" +
                    "('%" + "chars" + "%')";

    String SEARCH_BY_FILM_NAME =
            "SELECT F.*,\n" +
                    "       R.RATING_NAME\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R ON R.RATING_ID = F.RATING_ID\n" +
                    "WHERE UPPER(F.FILM_NAME) LIKE UPPER\n" +
                    "('%" + "chars" + "%')";

    String SQL_QUERY =
            "SELECT SEARCH.FILM_ID,\n" +
                    "SEARCH.FILM_NAME,\n" +
                    "SEARCH.FILM_DESCRIPTION,\n" +
                    "SEARCH.RELEASE_DATE,\n" +
                    "SEARCH.DURATION,\n" +
                    "SEARCH.RATING_ID,\n" +
                    "SEARCH.RATING_NAME,\n" +
                    "SEARCH.FILM_RATE\n" +
                    "FROM " + "(" + "string" + ") AS SEARCH\n" +
                    "LEFT JOIN LIKES AS L ON L.FILM_ID = SEARCH.FILM_ID\n" +
                    "GROUP BY SEARCH.FILM_ID\n" +
                    "ORDER BY SEARCH.FILM_RATE DESC, COUNT(L.FILM_ID) DESC";

    String SORTED_FILMS =
            "SELECT F.FILM_ID,\n" +
                    "       FILM_NAME,\n" +
                    "       FILM_DESCRIPTION,\n" +
                    "       RELEASE_DATE,\n" +
                    "       DURATION,\n" +
                    "       F.RATING_ID,\n" +
                    "       RATING_NAME,\n" +
                    "       FILM_RATE\n" +
                    "FROM FILMS F\n" +
                    "LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                    "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID\n" +
                    "GROUP BY F.FILM_ID\n" +
                    "ORDER BY FILM_RATE DESC, COUNT(L.FILM_ID) DESC\n";

    String UNION = "\nUNION\n";
    String DIRECTOR = "director";
    String TITLE = "title";

    Film create(Film film);

    Film update(Film film);

    void removeFilm(Integer filmId);

    void addLike(Integer filmId, Integer userId, Integer rate);

    void removeLike(Integer filmId, Integer userId);

    Collection<Film> findPopularFilms(Integer count);

    Collection<Film> findCommonFilms(Integer userId, Integer friendId);

    Collection<Film> getRecommendations(Integer userId);

    Collection<Film> findDirectorFilms(int directorId, String sortBy);

    Collection<Film> getTheMostPopularFilmsWithFilter(int count, Optional<Integer> genreId, Optional<Integer> year);

    Collection<Film> search(String query, List<String> searchFilters);
    Collection<Film> getSortedFilms();
}
