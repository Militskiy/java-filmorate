package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
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
