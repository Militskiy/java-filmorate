package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmValidator;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Integer filmId = 0;

    public Collection<Film> findAllFilms() {
        log.debug("Listing all films");
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        FilmValidator.validateFilm(film);
        film.setId(getNextId());
        filmStorage.createFilm(film);
        log.debug("Added new film {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        FilmValidator.validateFilm(film);
        if (filmStorage.findFilmById(film.getId()).isPresent()) {
            filmStorage.updateFilm(film);
            log.debug("Updated film with ID: {}", film.getId());
            return film;
        } else {
            throw new NoSuchFilmException("No film with such ID");
        }
    }

    public Integer addLike(Integer filmId, Integer userId) {
        if (filmStorage.findFilmById(filmId).isPresent()) {
            if (userStorage.findUserById(userId).isPresent()) {
                if (filmStorage.findFilmById(filmId).get().addLike(userId)) {
                    log.debug("User: {} liked Film: {}, total likes now: {}",
                            userStorage.findUserById(userId).get().getName(),
                            filmStorage.findFilmById(filmId).get().getName(),
                            filmStorage.findFilmById(filmId).get().getUserLikes().size());
                    return filmStorage.findFilmById(filmId).get().getUserLikes().size();
                } else {
                    throw new BadArgumentsException("Film already liked");
                }
            } else {
                throw new NoSuchUserException("No user with such ID");
            }
        } else {
            throw new NoSuchFilmException("No film with such ID");
        }
    }

    public Integer removeLike(Integer filmId, Integer userId) {
        if (filmStorage.findFilmById(filmId).isPresent()) {
            if (userStorage.findUserById(userId).isPresent()) {
                if (filmStorage.findFilmById(filmId).get().removeLike(userId)) {
                    log.debug("User: {} removed like from Film: {}, total likes now: {}",
                            userStorage.findUserById(userId).get().getName(),
                            filmStorage.findFilmById(filmId).get().getName(),
                            filmStorage.findFilmById(filmId).get().getUserLikes().size());
                    return filmStorage.findFilmById(filmId).get().getUserLikes().size();
                } else {
                    throw new BadArgumentsException("Cannot remove like, film is not liked");
                }
            } else {
                throw new NoSuchUserException("No user with such ID");
            }
        } else {
            throw new NoSuchFilmException("No film with such ID");
        }
    }

    public Collection<Film> findPopularFilms(Integer count) {
        log.debug("Listing {} popular films", count);
        return filmStorage.findAllFilms().stream()
                .sorted((f1, f2) -> f2.getUserLikes().size() - f1.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilm(Integer filmId) {
        return filmStorage.findFilmById(filmId).orElseThrow(() -> new NoSuchFilmException("No film with such ID"));
    }

    private Integer getNextId() {
        return ++filmId;
    }
}
