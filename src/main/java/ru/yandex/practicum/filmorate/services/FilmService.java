package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("FilmDaoImpl")
    private final FilmDao filmStorage;
    private final UserService userService;

    public Collection<Film> findAllFilms() {
        log.debug("Listing all films");
        return filmStorage.findAll();
    }

    public Film createFilm(Film film) {
        filmStorage.create(film);
        log.debug("Added new film {}", film);
        return film;
    }

    // проверки в storage
    public Film updateFilm(Film film) {
        filmStorage.update(film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) throws NoSuchFilmException, NoSuchUserException {
        userService.findUserById(userId);
        findFilmById(filmId);
        filmStorage.addLike(filmId, userId);
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
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getUserLikes().size() - f1.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer filmId) {
        return filmStorage.findById(filmId).orElseThrow(
                () -> new NoSuchFilmException("No Film with such ID: " + filmId)
        );
    }
}
