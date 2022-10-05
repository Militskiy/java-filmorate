package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Component
@Slf4j
public class DbMpaStorage implements MpaStorage {



    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> findById(Integer mpaId) {
         return jdbcTemplate.query(FIND_MPA_BY_ID, (rs, rowNum) -> makeMpa(rs), mpaId).stream().findFirst();
    }

    @Override
    public Collection<Mpa> findAll() {
        log.debug("Listing all MPA ratings");
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> makeMpa(rs));
    }
}
