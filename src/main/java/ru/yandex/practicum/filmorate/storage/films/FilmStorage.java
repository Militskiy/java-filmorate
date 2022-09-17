package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    void createFilm(Film film);

    void updateFilm(Film film);

    Optional<Film> findFilmById(Integer id);
}
