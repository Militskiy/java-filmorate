package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreStorage;

    public Genre findById(Integer genreId) {
        return genreStorage.findById(genreId);
    }

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }
}
