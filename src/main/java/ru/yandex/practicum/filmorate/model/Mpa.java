package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @Schema(type = "integer", example = "1")
    int id;
    @NotNull
    @Schema(type = "string", example = "G")
    private String name;
}
