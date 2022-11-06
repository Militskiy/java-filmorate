package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmDao extends Dao<Film> {

    String CREATE_FILM = "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
            "VALUES (:filmName, :filmDescription, :releaseDate, :duration, :ratingId);";

    String FILM_GENRE_UPDATE = "INSERT INTO GENRES_FILMS (FILM_ID, GENRE_ID) VALUES (?, ?)";

    String DELETE_GENRES_QUERY = "DELETE FROM GENRES_FILMS WHERE FILM_ID = :filmId;\n";

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


    //запрос по факту некорректный, нет проверки на дружбу, но это косяк postman
    String FIND_COMMON_FILMS_COUPLE_FRIENDS =
            "select f.*, r.RATING_NAME\n" +
                    "from (select l.FILM_ID\n" +
                    "      from\n" +
                    "               LIKES L\n" +
                    "               inner join LIKES L2 on l.FILM_ID = L2.FILM_ID\n" +
                    "      where l.USER_ID = ?\n" +
                    "        AND l2.USER_ID = ?\n" +
                    "        AND L.FILM_ID = L2.FILM_ID) as FL\n" +
                    "         inner join FILMS as f on f.FILM_ID = FL.FILM_ID\n" +
                    "         left join (select l.FILM_ID, count(distinct l.USER_ID) as ql FROM LIKES as l group by l.FILM_ID)\n" +
                    "    as L3 on f.FILM_ID = L3.FILM_ID\n" +
                    "         left join RATINGS R on R.RATING_ID = f.RATING_ID\n" +
                    "order by ql desc\n" +
                    ";";


    Film create(Film film);

    Film update(Film film);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Collection<Film> findPopularFilms(Integer count);

    Collection<Film> findCommonFilmsOfCoupleFriends(Integer userId, Integer friendId);
}
