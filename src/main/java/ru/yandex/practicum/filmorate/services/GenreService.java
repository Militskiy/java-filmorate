package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.GenreDao;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreStorage;

    public Genre findById(Integer genreId) {
        return genreStorage.findById(genreId).orElseThrow(
                () -> new NoSuchGenreException("There is no genre with ID: " + genreId));
    }

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }
}
