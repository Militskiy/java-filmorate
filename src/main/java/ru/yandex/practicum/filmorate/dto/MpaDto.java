package ru.yandex.practicum.filmorate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class MpaDto {
    @Schema(type = "integer", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    int id;
    @NotNull
    @Schema(type = "string", example = "G", accessMode = Schema.AccessMode.READ_ONLY)
    String name;
}
