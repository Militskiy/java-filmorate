package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.services.GenreService;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Genre services")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @Operation(summary = "Get a list of all genres")
    public ResponseEntity<Collection<GenreDto>> findAll() {
        log.debug("Getting all genres");
        return ResponseEntity.ok(
                genreService.findAll()
                        .stream()
                        .map(GenreMapper.INSTANCE::genreToGenreDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific genre by its id")
    public ResponseEntity<GenreDto> findGenre(@PathVariable @Min(1) Integer id) {
        log.debug("Getting genre with id: {}", id);
        return ResponseEntity.ok(GenreMapper.INSTANCE.genreToGenreDto(genreService.findById(id)));
    }
}
