package ru.yandex.practicum.filmorate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.validators.NullCheckGroup;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class DirectorDto {
    @Schema(example = "1", required = true)
    int id;
    @NotBlank(message = "Name must not be blank", groups = NullCheckGroup.class)
    @Schema(example = "Test director", required = true)
    String name;
}
