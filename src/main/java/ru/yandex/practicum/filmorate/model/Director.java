package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.NullCheckGroup;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Director {
    @EqualsAndHashCode.Include
    @Schema(example = "1", required = true)
    private int id;
    @NotBlank(message = "Name must not be blank", groups = NullCheckGroup.class)
    @Schema(example = "Test director", required = true)
    private String name;

    public Director(String name) {
        this.name = name;
    }
}
