package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class FilmControllerTest implements TestJsons {

    private static final LocalDate TEST_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FilmController filmController;

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void shouldFindAllFilms() throws Exception {
        Film film1 = new Film("name", "1".repeat(200), TEST_DATE, 1, new Mpa(1, "G"));
        Film film2 = new Film("name", null, TEST_DATE, 1, new Mpa(1, "G"));
        Film film3 = new Film("name", "", TEST_DATE, 1, new Mpa(1, "G"));
        film1 = filmController.createFilm(film1);
        film2 = filmController.createFilm(film2);
        film3 = filmController.createFilm(film3);
        String listBody = objectMapper.writeValueAsString(List.of(film1, film2, film3));
        this.mockMvc.perform(
                        get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(listBody));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldFindFilm1() throws Exception {
        this.mockMvc.perform(
                get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("film1"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void shouldCreateFilm() throws Exception {
        Film film = new Film("name", "description", TEST_DATE, 1, new Mpa(1, "G"));
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        String listBody = objectMapper.writeValueAsString(filmController.findAllFilms());
        this.mockMvc.perform(
                        get("/films"))
                .andExpect(content().json(listBody));
    }

    @Test
    void tryToCreateFilmWithEmptyNameBadRequest() throws Exception {
        Film film = new Film("", RandomString.make(200), TEST_DATE, 1, new Mpa(1, "G"));
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateFilmWithEarlyDateBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200),
                TEST_DATE.minusDays(1), 1, new Mpa(1, "G"));
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateFilmWithFutureDateBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200),
                LocalDate.now().plusDays(1), 1, new Mpa(1, "G"));
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateFilmWithNullDateBadRequest() throws Exception {
        Film film = new Film(
                "name",
                RandomString.make(200),
                null,
                1,
                new Mpa(1, "G")
        );
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateFilmWithLongDescriptionBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(201), TEST_DATE, 1, new Mpa(1, "G"));
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateFilmWithNegativeDurationBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(200), TEST_DATE, -1, new Mpa(1, "G"));
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToUpdateFilmWithWrongIdNotFound() throws Exception {
        Film film = new Film(
                10,
                "name",
                RandomString.make(200),
                TEST_DATE,
                1,
                new Mpa(1, "G")
        );
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof NoSuchFilmException))
                .andExpect(result -> assertEquals("No Film with such ID: 10",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void createsAndUpdatesFilm() throws Exception {
        Film film = new Film(
                "name",
                RandomString.make(200),
                TEST_DATE,
                1,
                new Mpa(1, "G")
        );
        filmController.createFilm(film);
        Film updatedFilm = new Film(
                film.getId(),
                "updated",
                RandomString.make(1),
                TEST_DATE,
                2,
                new Mpa(1, "G")
        );
        String body = objectMapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldListPopularFilms() throws Exception {
        this.mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(content().json(POPULAR));
    }
    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldAddLikeToFilmWithId1() throws Exception {
        this.mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());
        assertEquals("" +
                "[User(id=3, " +
                "email=email3@test.com, " +
                "login=login3, " +
                "name=name3, " +
                "birthday=2022-10-01, " +
                "friends=[]), " +
                "User(id=1, " +
                "email=email1@test.com, " +
                "login=login1, " +
                "name=name1, " +
                "birthday=2022-10-01, " +
                "friends=[])]",
                filmController.findFilm(1).getUserLikes().toString()
        );
    }
    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldRemoveLikeFromFilmWithId1() throws Exception {
        this.mockMvc.perform(delete("/films/1/like/3"))
                .andExpect(status().isOk());
        Assertions.assertEquals(Set.of(), filmController.findFilm(1).getUserLikes());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void tryRemoveLikeNotFound() throws Exception {
        this.mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void tryAddLikeAlreadyLiked() throws Exception {
        this.mockMvc.perform(put("/films/3/like/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldRemove5thGenreFromFilm1() throws Exception {
        Film film = filmController.findFilm(1);
        film.removeGenre(new Genre(5, "Документальный"));
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldAddGenreToFilm() throws Exception {
        Film film = filmController.findFilm(3);
        film.addGenre(new Genre(5, "Документальный"));
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowBadRequestWhenAddingWrongGenre() throws Exception {
        Film film = filmController.findFilm(3);
        film.addGenre(new Genre(10, "No such genre"));
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
