package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    static Film film;
    private static final LocalDate TEST_DATE = LocalDate.of(1895, 12, 28);
    FilmController filmController;

    @BeforeAll
    static void beforeAll() {
        film = new Film("name", "description", TEST_DATE, 1);
    }

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void findAllFilms() {
        filmController.createFilm(film);
        filmController.createFilm(new Film("name", "description", TEST_DATE, 1));
        filmController.createFilm(new Film("name", "description", TEST_DATE, 1));
        assertEquals(3, filmController.findAllFilms().size());
    }

    @Test
    void throwsValidationErrorEmptyFilmName() {
        final Film emptyNameFilm = new Film(" ", "description", TEST_DATE, 100);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(emptyNameFilm));
        assertEquals("Film name cannot be empty", ex.getMessage());
    }

    @Test
    void throwsValidationErrorEarlyDate() {
        final Film earlyDateFilm = new Film("name", "description", TEST_DATE.minusDays(1), 100);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(earlyDateFilm));
        assertEquals("Film release date is before 28.12.1985", ex.getMessage());
    }
    @Test
    void throwsValidationErrorFutureDate() {
        final Film futureDateFilm = new Film("name", "description", LocalDate.now().plusDays(1), 100);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(futureDateFilm));
        assertEquals("Film release date must be in the past", ex.getMessage());
    }

    @Test
    void throwsValidationErrorNullDate() {
        final Film nullDateFilm = new Film("name", "description", null, 100);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(nullDateFilm));
        assertEquals("Film release date cannot be null", ex.getMessage());
    }

    @Test
    void throwsValidationErrorNotPositiveDuration() {
        final Film futureDateFilm = new Film("name", "description", TEST_DATE, 0);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(futureDateFilm));
        assertEquals("Film duration must be positive", ex.getMessage());
    }

    @Test
    void throwsValidationErrorLongDescription() {
        String description = "1".repeat(201);
        final Film longDescriptionName = new Film("name", description, TEST_DATE, 100);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(longDescriptionName));
        assertEquals("Film description cannot be longer then 200 symbols", ex.getMessage());
    }

    @Test
    void updatesFilm() {
        filmController.createFilm(film);
        assertEquals(1, filmController.findAllFilms().size());
        final Film updatedFilm = new Film(1, "name", "updated_description", TEST_DATE, 120);
        filmController.updateFilm(updatedFilm);
        assertEquals(1, filmController.findAllFilms().size());
        assertEquals(updatedFilm, filmController.findAllFilms().get(0));
    }
}
