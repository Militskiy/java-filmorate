package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public final class FilmValidator {

    private FilmValidator() {
    }

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new BadArgumentsException("Film name cannot be empty");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new BadArgumentsException("Film description cannot be longer then 200 symbols");
        }
        if (film.getReleaseDate() == null) {
            throw new BadArgumentsException("Film release date cannot be null");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new BadArgumentsException("Film release date must be after 28.12.1985");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new BadArgumentsException("Film release date must be in the past");
        }
        if (film.getDuration() <= 0) {
            throw new BadArgumentsException("Film duration must be positive");
        }

    }

}
