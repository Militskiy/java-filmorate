package ru.yandex.practicum.filmorate.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@Mapper
public interface MpaMapper {
    MpaMapper INSTANCE = Mappers.getMapper(MpaMapper.class);

    MpaDto mpaToMpaDto(Mpa mpa);
}
