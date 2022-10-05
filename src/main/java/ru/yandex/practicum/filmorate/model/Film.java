package ru.yandex.practicum.filmorate.model;

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
    private String name;
    @Size(max = 200, message = "Description size must be under 200 symbols")
    private String description;
    @NotNull(message = "Release date must not be null", groups = NullCheckGroup.class)
    @PastOrPresent(message = "Release date cannot be in the future")
    @FilmDateConstraint
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive")
    private long duration;
    private BaseEntity mpa;
    private final Set<BaseEntity> genres = new TreeSet<>(Comparator.comparingInt(BaseEntity::getId));
    private final Set<Integer> userLikes = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public boolean addLike(Integer userId) {
        return userLikes.add(userId);
    }

    public boolean addGenre(Genre genre) {
        return genres.add(genre);
    }

    public boolean removeLike(Integer userId) {
        return userLikes.remove(userId);
    }
}
