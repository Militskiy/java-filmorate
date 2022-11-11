package ru.yandex.practicum.filmorate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.annotations.FilmDateConstraint;
import ru.yandex.practicum.filmorate.validators.NullCheckGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Value
@Builder
@Jacksonized
public class FilmDto {
    int id;
    @NotBlank(message = "Name must not be blank", groups = NullCheckGroup.class)
    @Schema(example = "Test film")
    String name;
    @Size(max = 200, message = "Description size must be under 200 symbols")
    @Schema(example = "A film to test application")
    String description;
    @NotNull(message = "Release date must not be null", groups = NullCheckGroup.class)
    @FilmDateConstraint
    @Schema(example = "2000-10-22")
    LocalDate releaseDate;
    @Positive(message = "Duration must be positive")
    @Schema(type = "integer", example = "120")
    long duration;
    @NotNull
    MpaDto mpa;
    @Schema(example = "4")
    int rate;
    @Singular
    Set<GenreDto> genres;
    @Schema(example = "[]")
    @Singular
    Set<UserDto> userLikes;
    @Singular
    Set<DirectorDto> directors;
}
