package ru.yandex.practicum.filmorate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class GenreDto {
    @Schema(type = "integer", example = "1")
    int id;
    @NotNull
    @Schema(type = "string", example = "Комедия")
    String name;
}
