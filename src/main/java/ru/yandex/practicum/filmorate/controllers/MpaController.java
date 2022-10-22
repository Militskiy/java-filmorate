package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.services.MpaService;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "MPA rating services")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    @Operation(summary = "Get a list of all MPA ratings")
    public Collection<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific MPA rating by its id")
    public Mpa findMpa(@PathVariable @Min(1) Integer id) {
        return mpaService.findById(id);
    }
}
