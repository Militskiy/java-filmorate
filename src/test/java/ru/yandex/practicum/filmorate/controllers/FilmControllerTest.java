package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    private static final LocalDate TEST_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private FilmController filmController;

    @Test
    void shouldFindAllFilms() throws Exception {
        Film film1 = new Film("name", RandomString.make(200), TEST_DATE, 1);
        Film film2 = new Film("name", null, TEST_DATE, 1);
        Film film3 = new Film("name", "", TEST_DATE, 1);
        String body = objectMapper.writeValueAsString(List.of(film1, film2, film3));
        Mockito.when(filmController.findAllFilms()).thenReturn(List.of(film1, film2, film3));
        this.mockMvc.perform(
                get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void tryToCreateFilmWithEmptyNameBadRequest() throws Exception {
        Film film = new Film("", RandomString.make(200), TEST_DATE, 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateFilmWithEarlyDateBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200), TEST_DATE.minusDays(1), 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateFilmWithFutureDateBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200), LocalDate.now().plusDays(1), 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateFilmWithNullDateBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200), null, 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateFilmWithLongDescriptionBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(201), TEST_DATE, 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateFilmWithNegativeDurationBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200), TEST_DATE, -1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToUpdateFilmWithWrongIdNotFound() throws Exception {
        Film film = new Film(10,"name", RandomString.make(200), TEST_DATE, 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsAndUpdatesFilm() throws Exception {
        Film film = new Film("name", RandomString.make(200), TEST_DATE, 1);
        filmController.createFilm(film);
        Film updatedFilm = new Film(film.getId(), "updated", RandomString.make(1), TEST_DATE, 2);
        String body = objectMapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }
}

