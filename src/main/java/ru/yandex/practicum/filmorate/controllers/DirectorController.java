package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.services.DirectorService;
import ru.yandex.practicum.filmorate.validators.ValidationSequence;

import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Director services")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @Operation(summary = "Get a list of all directors")
    public List<Director> getDirectors() {
        log.debug("Getting a list of all directors");
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific director by its id")
    public Director getDirector(@PathVariable int id) {
        log.debug("Getting a specific director by its id");
        return directorService.getDirectorById(id);
    }

    @PostMapping
    @Operation(summary = "Add a new director")
    public Director createDirector(@Validated(ValidationSequence.class) @RequestBody Director director) {
        log.debug("Adding a new director");
        return directorService.createDirector(director);
    }

    @PutMapping
    @Operation(summary = "Update a director")
    public Director updateDirector(@Validated(ValidationSequence.class)  @RequestBody Director director) {
        log.debug("Updating a director");
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific director by its id")
    public void deleteDirector(@PathVariable int id) {
        log.debug("Deleting director");
        directorService.deleteDirector(id);
    }

}
