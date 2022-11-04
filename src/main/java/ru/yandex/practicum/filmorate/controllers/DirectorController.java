package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.services.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Tag(name = "Director services")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @Operation(summary = "Get a list of all directors")
    public List<Director> getDirectors() {
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific director by its id")
    public Director getDirector(@PathVariable int id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    @Operation(summary = "Add a new director to service")
    public Director createDirector(@Valid @RequestBody Director director) {
        return directorService.createDirector(director);
    }

    @PutMapping
    @Operation(summary = "Update a director")
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a director")
    public void deleteDirector(@PathVariable int id) {
        directorService.deleteDirector(id);
    }

}
