package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

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

    public List<Film> getDirectorFilmsSorted(int directorId, String sortBy) {
        return filmStorage.findDirectorFilms(directorId, sortBy);
    }
}
