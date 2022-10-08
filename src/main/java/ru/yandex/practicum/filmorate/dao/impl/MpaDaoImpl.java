package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {



    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> findById(Integer mpaId) {
        log.debug("Listing rating with id: {}", mpaId);
         return jdbcTemplate.query(FIND_MPA_BY_ID, (rs, rowNum) -> makeMpa(rs), mpaId).stream().findFirst();
    }

    @Override
    public Collection<Mpa> findAll() {
        log.debug("Listing all MPA ratings");
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> makeMpa(rs));
    }
}
