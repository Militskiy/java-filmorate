package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.services.DirectorService;
import ru.yandex.practicum.filmorate.validators.ValidationSequence;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Director services")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @Operation(summary = "Get a list of all directors")
    public ResponseEntity<List<DirectorDto>> getDirectors() {
        log.debug("Getting a list of all directors");
        return ResponseEntity.ok(
                directorService.getDirectors()
                        .stream()
                        .map(DirectorMapper.INSTANCE::directorToDirectorDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific director by its id")
    public ResponseEntity<DirectorDto> getDirector(@PathVariable int id) {
        log.debug("Getting a specific director by its id");
        return ResponseEntity.ok(DirectorMapper.INSTANCE.directorToDirectorDto(directorService.getDirectorById(id)));
    }

    @PostMapping
    @Operation(summary = "Add a new director")
    public ResponseEntity<DirectorDto> createDirector(@Validated(ValidationSequence.class) @RequestBody DirectorDto directorDto) {
        log.debug("Adding a new director");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                DirectorMapper.INSTANCE.directorToDirectorDto(
                        directorService.createDirector(DirectorMapper.INSTANCE.directorDtoToDirector(directorDto))
                )
        );
    }

    @PutMapping
    @Operation(summary = "Update a director")
    public ResponseEntity<DirectorDto> updateDirector(
            @Validated(ValidationSequence.class) @RequestBody DirectorDto directorDto
    ) {
        log.debug("Updating a director");
        return ResponseEntity.ok(
                DirectorMapper.INSTANCE.directorToDirectorDto(
                        directorService.updateDirector(DirectorMapper.INSTANCE.directorDtoToDirector(directorDto))
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific director by its id")
    public void deleteDirector(@PathVariable int id) {
        log.debug("Deleting director");
        directorService.deleteDirector(id);
    }

}
