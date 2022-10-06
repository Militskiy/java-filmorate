package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

public interface MpaDao extends Dao<Mpa> {

    String FIND_MPA_BY_ID =
            "SELECT * " +
                    "FROM RATINGS " +
                    "WHERE RATING_ID = ?";
    String FIND_ALL =
            "SELECT * " +
                    "FROM RATINGS";
}
