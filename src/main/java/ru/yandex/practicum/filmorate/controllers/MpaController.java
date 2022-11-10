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
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.services.MpaService;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "MPA rating services")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    @Operation(summary = "Get a list of all MPA ratings")
    public ResponseEntity<Collection<MpaDto>> findAll() {
        log.debug("Getting all MPA ratings");
        return ResponseEntity.ok(
                mpaService.findAll()
                        .stream()
                        .map(MpaMapper.INSTANCE::mpaToMpaDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific MPA rating by its id")
    public ResponseEntity<MpaDto> findMpa(@PathVariable @Min(1) Integer id) {
        log.debug("Getting rating with id: {}", id);
        return ResponseEntity.ok(MpaMapper.INSTANCE.mpaToMpaDto(mpaService.findById(id)));
    }
}
