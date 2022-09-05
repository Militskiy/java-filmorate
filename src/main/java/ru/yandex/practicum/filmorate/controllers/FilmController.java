package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> findAllFilms() {
        log.debug("Listing all films");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        FilmValidator.validateFilm(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.debug("Added new film {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        FilmValidator.validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new NoSuchFilmException("No film with such ID");
        }
        films.put(film.getId(), film);
        log.debug("Updated film with ID: {}", film.getId());
        return film;
    }
}
