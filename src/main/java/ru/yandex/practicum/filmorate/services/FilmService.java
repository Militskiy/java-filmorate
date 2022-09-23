package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private Integer filmId = 0;

    public Collection<Film> findAllFilms() {
        log.debug("Listing all films");
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        film.setId(getNextId());
        filmStorage.createFilm(film);
        log.debug("Added new film {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (filmStorage.findFilmById(film.getId()).isPresent()) {
            filmStorage.updateFilm(film);
            log.debug("Updated film with ID: {}", film.getId());
            return film;
        } else {
            throw new NoSuchFilmException("No film with such ID: " + film.getId());
        }
    }

    public Integer addLike(Integer filmId, Integer userId) throws NoSuchFilmException, NoSuchUserException {
        User user = userService.findUserById(userId);
        Film film = findFilmById(filmId);
        if (film.addLike(userId)) {
            log.debug("User: {} liked Film: {}, total likes now: {}",
                    user.getName(),
                    film.getName(),
                    film.getUserLikes().size());
            return film.getUserLikes().size();
        } else {
            throw new BadArgumentsException("Film already liked");
        }
    }

    public Integer removeLike(Integer filmId, Integer userId) throws NoSuchFilmException, NoSuchUserException {
        User user = userService.findUserById(userId);
        Film film = findFilmById(filmId);
        if (film.removeLike(userId)) {
            log.debug("User: {} removed like from Film: {}, total likes now: {}",
                    user.getName(),
                    film.getName(),
                    film.getUserLikes().size());
            return film.getUserLikes().size();
        } else {
            throw new BadArgumentsException("Cannot remove like, film is not liked");
        }
    }

    public Collection<Film> findPopularFilms(Integer count) {
        log.debug("Listing {} popular films", count);
        return filmStorage.findAllFilms().stream()
                .sorted((f1, f2) -> f2.getUserLikes().size() - f1.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer filmId) {
        return filmStorage.findFilmById(filmId).orElseThrow(
                () -> new NoSuchFilmException("No film with such ID: " + filmId));
    }

    private Integer getNextId() {
        return ++filmId;
    }
}
