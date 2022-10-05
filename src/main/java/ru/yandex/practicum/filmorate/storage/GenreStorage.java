package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

public interface GenreStorage extends Storage<Genre> {

    String FIND_GENRE_BY_ID =
            "SELECT * " +
                    "FROM GENRES " +
                    "WHERE GENRE_ID = ?";
    String FIND_ALL =
            "SELECT * " +
                    "FROM GENRES";
    Optional<Genre> findById(Integer genreId);
}
