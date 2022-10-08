package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.MpaDao;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaStorage;

    public Mpa findById(Integer mpaId) {
        return mpaStorage.findById(mpaId).orElseThrow(
                () -> new NoSuchMpaException("There is no rating with ID: " + mpaId));
    }

    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
