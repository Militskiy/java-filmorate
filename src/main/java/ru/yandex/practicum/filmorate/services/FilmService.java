package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static ru.yandex.practicum.filmorate.dao.FilmDao.DIRECTOR;
import static ru.yandex.practicum.filmorate.dao.FilmDao.TITLE;

@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("FilmDaoImpl")
    private final FilmDao filmStorage;

    public Collection<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    // проверки в storage
    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public void deleteFilm(Integer filmId) {
        filmStorage.removeFilm(filmId);
    }

    public void addLike(Integer filmId, Integer userId) throws NoSuchFilmException, NoSuchUserException {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) throws NoSuchFilmException, NoSuchUserException {
        filmStorage.removeLike(filmId, userId);
    }

    public Collection<Film> findPopularFilms(Integer count) {
        return filmStorage.findPopularFilms(count);
    }

    public Film findFilmById(Integer filmId) {
        return filmStorage.findById(filmId);
    }

    public Collection<Film> findCommonFilmsOfCoupleFriends(Integer userdId, Integer friendId) {
        return filmStorage.findCommonFilmsOfCoupleFriends(userdId,friendId);
    }

    public List<Film> getDirectorFilmsSorted(int directorId, String sortBy) {
        return filmStorage.findDirectorFilms(directorId, sortBy);
    }

    public List<Film> search(String query, List<String> searchFilters) {

        if (searchFilters.size() > 2) {
            throw new BadArgumentsException("Search filters are out of the request limits (max 2 filters).");
        }

        if (searchFilters.size() > new HashSet<>(searchFilters).size()) {
            throw new BadArgumentsException("Search request has filter repeats.");
        }

        if (searchFilters.size() == 1) {
            if (!searchFilters.contains(DIRECTOR)) {
                if (!searchFilters.contains(TITLE)) {
                    throw new BadArgumentsException("Incorrect filter. Should be - title.");
                }
            } else if (!searchFilters.contains(TITLE)) {
                if (!searchFilters.contains(DIRECTOR)) {
                    throw new BadArgumentsException("Incorrect filter. Should be - director.");
                }
            }
        }

        if (searchFilters.size() == 2) {
            if (!searchFilters.contains(DIRECTOR) && !searchFilters.contains(TITLE)) {
                throw new BadArgumentsException("Incorrect filters. Should be - director and title.");
            }
        }

        return filmStorage.search(query, searchFilters);
    }
}
