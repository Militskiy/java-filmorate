package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.FilmDateConstraint;
import ru.yandex.practicum.filmorate.validators.NullCheckGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank(message = "Name must not be blank", groups = NullCheckGroup.class)
    @Schema(example = "Test film")
    private String name;
    @Size(max = 200, message = "Description size must be under 200 symbols")
    @Schema(example = "A film to test application")
    private String description;
    @NotNull(message = "Release date must not be null", groups = NullCheckGroup.class)
    @PastOrPresent(message = "Release date cannot be in the future")
    @FilmDateConstraint
    @Schema(example = "2000-10-22")
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive")
    @Schema(type = "integer", example = "120")
    private long duration;
    @NotNull
    @Schema(example = "{\"id\": 1}")
    private Mpa mpa;
    @Schema(example = "[{\"id\": 1}]")
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    @Schema(example = "[]")
    private final Set<User> userLikes = new HashSet<>();
    @Schema(example = "{\"id\": 1}")
    private Director director;

    public Film(String name, String description, LocalDate releaseDate, long duration, Mpa mpa, Director director) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.director = director;
    }

    public boolean addLike(User user) {
        return userLikes.add(user);
    }

    public boolean addGenre(Genre genre) {
        return genres.add(genre);
    }
    public boolean removeGenre(Genre genre) {
        return genres.remove(genre);
    }
}
