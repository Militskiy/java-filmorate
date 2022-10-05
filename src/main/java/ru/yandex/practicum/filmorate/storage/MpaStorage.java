package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

public interface MpaStorage extends Storage<Mpa> {

    String FIND_MPA_BY_ID =
            "SELECT * " +
                    "FROM RATINGS " +
                    "WHERE RATING_ID = ?";
    String FIND_ALL =
            "SELECT * " +
                    "FROM RATINGS";

    Optional<Mpa> findById(Integer mpaId);
}
