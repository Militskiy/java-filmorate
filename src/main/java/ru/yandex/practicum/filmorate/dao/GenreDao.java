package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreDao extends Dao<Genre> {

    String FIND_GENRE_BY_ID =
            "SELECT * " +
                    "FROM GENRES " +
                    "WHERE GENRE_ID = ?";
    String FIND_ALL =
            "SELECT * " +
                    "FROM GENRES";
}
