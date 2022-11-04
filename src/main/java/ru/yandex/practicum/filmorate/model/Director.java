package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Director {
    @EqualsAndHashCode.Include
    @Schema(example = "1", required = true)
    private int id;
    @NotNull
    @Schema(example = "Test director", required = true)
    private String name;

    public Director(String name) {
        this.name = name;
    }
}
