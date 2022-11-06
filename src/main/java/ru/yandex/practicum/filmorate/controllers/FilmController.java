package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.validators.ValidationSequence;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Film services")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    @Operation(summary = "Get all films")
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get film by its id")
    public Film findFilm(@PathVariable @Min(1) Integer id) {
        return filmService.findFilmById(id);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get a sorted list of films by popularity")
    public Collection<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count
    ) {
        return filmService.findPopularFilms(count);
    }

    @PostMapping
    @Operation(summary = "Add a new film to service")
    public Film createFilm(@Validated(ValidationSequence.class) @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    @Operation(summary = "Update a film")
    public Film updateFilm(@Validated(ValidationSequence.class) @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Add a user like to a film")
    public void addLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Remove a user like from a film")
    public void removeLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/common")
    @Operation(summary = "Get a sorted common list of couple friends by popularity")
    public Collection<Film> findCommonFilmsOfCoupleFriends(
            @RequestParam(value = "userId",required = true) Integer userId,
            @RequestParam(value = "friendId", required = true) Integer friendId
    ) {
        return filmService.findCommonFilmsOfCoupleFriends(userId, friendId);
    }


}
