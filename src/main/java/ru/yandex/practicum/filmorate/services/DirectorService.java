package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.exceptions.NoSuchDirectorException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorDAO directorStorage;

    public List<Director> getDirectors() {
        return directorStorage.findAll();
    }

    public Director getDirectorById(int id) {
        return directorStorage.findById(id)
                .orElseThrow(() -> new NoSuchDirectorException(String.format("Директор с id = %d не найден", id)));
    }

    public Director createDirector(Director director) {
        return directorStorage.create(director);
    }

    public Director updateDirector(Director director) {
        int id = director.getId();
        directorStorage.findById(id)
                .orElseThrow(() -> new NoSuchDirectorException(String.format("Директор с id = %d не найден", id)));

        return directorStorage.update(director);
    }

    public void deleteDirector(int id) {
        directorStorage.findById(id)
                .orElseThrow(() -> new NoSuchDirectorException(String.format("Директор с id = %d не найден", id)));
        directorStorage.delete(id);
    }
}
