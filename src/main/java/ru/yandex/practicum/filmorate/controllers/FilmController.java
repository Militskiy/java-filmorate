package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.validators.ValidationSequence;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Film services")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    @Operation(summary = "Get all films")
    public ResponseEntity<Collection<FilmDto>> findAllFilms() {
        log.debug("Getting all films");
        return ResponseEntity.ok(
                filmService.findAllFilms()
                        .stream()
                        .map(FilmMapper.INSTANCE::filmToFilmDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get film by its id")
    public ResponseEntity<FilmDto> findFilm(@PathVariable @Min(1) Integer id) {
        log.debug("Getting film with id: {}", id);
        return ResponseEntity.ok(FilmMapper.INSTANCE.filmToFilmDto(filmService.findFilmById(id)));
    }

    @DeleteMapping("/{filmId}")
    @Operation(summary = "Delete film by its id")
    public void removeFilm(@PathVariable Integer filmId) {
        log.debug("Deleting film with id: {}", filmId);
        filmService.deleteFilm(filmId);
    }


    @PostMapping
    @Operation(summary = "Add a new film to service")
    public ResponseEntity<FilmDto> createFilm(@Validated(ValidationSequence.class) @RequestBody FilmDto filmDto) {
        log.debug("Creating new film {}", filmDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        FilmMapper.INSTANCE.filmToFilmDto(
                                filmService.createFilm(FilmMapper.INSTANCE.filmDtoToFilm(filmDto))
                        )
                );
    }

    @PutMapping
    @Operation(summary = "Update a film")
    public ResponseEntity<FilmDto> updateFilm(@Validated(ValidationSequence.class) @RequestBody FilmDto film) {
        log.debug("Updating film with id: {}", film.getId());
        return ResponseEntity.ok(FilmMapper.INSTANCE.filmToFilmDto(
                filmService.updateFilm(FilmMapper.INSTANCE.filmDtoToFilm(film))
        ));
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Add a user like to a film")
    public void addLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId,
            @RequestParam(defaultValue = "6", required = false) @Min(1) @Max(10) Integer rate
    ) {
        log.debug("Adding user with id: {} like to film with id: {}", userId, id);
        filmService.addLike(id, userId, rate);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Remove a user like from a film")
    public void removeLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        log.debug("Removing user with id: {} like from film with id: {}", userId, id);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/director/{directorId}")
    @Operation(summary = "Get all films by director id")
    public ResponseEntity<Collection<FilmDto>> getDirectorFilmsSorted(
            @PathVariable int directorId,
            @RequestParam(defaultValue = "year") String sortBy
    ) {
        log.debug("Getting all films by director");
        return ResponseEntity.ok(filmService.getDirectorFilmsSorted(directorId, sortBy)
                .stream()
                .map(FilmMapper.INSTANCE::filmToFilmDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/popular")
    @Operation(summary = "Get the most popular films with filter: year, genre")
    public ResponseEntity<Collection<FilmDto>> getTheMostPopularFilmsWithFilter(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer limit,
            @RequestParam(value = "genreId") Optional<Integer> genreId,
            @RequestParam(value = "year") Optional<Integer> year
    ) {
        if (genreId.isPresent() || year.isPresent()) {
            log.debug("Getting the most popular films with filter");
            return ResponseEntity.ok(filmService.getTheMostPopularFilmsWithFilter(limit, genreId, year)
                    .stream()
                    .map(FilmMapper.INSTANCE::filmToFilmDto)
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.ok(filmService.findPopularFilms(limit)
                .stream()
                .map(FilmMapper.INSTANCE::filmToFilmDto)
                .collect(Collectors.toList()));
    }


    @GetMapping("/search")
    @Operation(summary = "Getting films sorted by filters")
    public ResponseEntity<Collection<FilmDto>> search(
            @RequestParam String query,
            @RequestParam List<String> by
    ) {
        log.debug("Getting sorted films by filters");
        return ResponseEntity.ok(filmService.search(query, by)
                .stream()
                .map(FilmMapper.INSTANCE::filmToFilmDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/common")
    @Operation(summary = "Get a film list of common films between two users, sorted by popularity")
    public ResponseEntity<Collection<FilmDto>> findCommonFilmList(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "friendId") Integer friendId
    ) {
        return ResponseEntity.ok(filmService.findCommonFilms(userId, friendId)
                .stream()
                .map(FilmMapper.INSTANCE::filmToFilmDto)
                .collect(Collectors.toList()));
    }
}
