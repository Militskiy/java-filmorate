package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@AllArgsConstructor
@Component
public class MpaDaoImpl implements MpaDao {



    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findById(Integer mpaId) {
         return jdbcTemplate.query(FIND_MPA_BY_ID, (rs, rowNum) -> makeMpa(rs), mpaId)
                 .stream()
                 .findAny()
                 .orElseThrow(() -> new NoSuchMpaException("There is no rating with ID: " + mpaId));
    }

    @Override
    public Collection<Mpa> findAll() {
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> makeMpa(rs));
    }
}
