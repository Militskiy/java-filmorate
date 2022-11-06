package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaStorage;

    public Mpa findById(Integer mpaId) {
        return mpaStorage.findById(mpaId);
    }

    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
