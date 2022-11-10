package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
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
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Objects;

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

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void shouldFindAllFilms() throws Exception {
        this.mockMvc.perform(
                        get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
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
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldCreateFilm() throws Exception {
        FilmDto film = FilmDto.builder()
                .name("name")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .build();
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));
        this.mockMvc.perform(
                        get("/films/4"))
                .andExpect(content().json("{" +
                        "\"id\":4," +
                        "\"name\":\"name\"," +
                        "\"description\":\"description\"," +
                        "\"releaseDate\":\"1895-12-28\"," +
                        "\"duration\":1," +
                        "\"mpa\":{\"id\":1,\"name\":\"G\"}," +
                        "\"genres\":[]," +
                        "\"userLikes\":[]," +
                        "\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]}"));
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
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void createsAndUpdatesFilm() throws Exception {
        FilmDto film = FilmDto.builder()
                .id(2)
                .name("updated")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .build();
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(UPDATED_TEST_FILM));
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
        this.mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(TEST_FILM_1_WITH_LIKE));
    }
    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldRemoveLikeFromFilmWithId1() throws Exception {
        this.mockMvc.perform(delete("/films/1/like/3"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(TEST_FILM_1_REMOVED_LIKE));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void tryRemoveLikeBadRequest() throws Exception {
        this.mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void tryAddLikeAlreadyLiked() throws Exception {
        this.mockMvc.perform(put("/films/3/like/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldRemoveGenreFromFilm4() throws Exception {
        FilmDto film = FilmDto.builder()
                .name("name")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .genre(GenreDto.builder().id(1).build())
                .build();
        String filmBody = objectMapper.writeValueAsString(film);
        FilmDto updatedFilm = FilmDto.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .build();
        String updatedFilmBody = objectMapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(post("/films").content(filmBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        this.mockMvc.perform(put("/films").content(updatedFilmBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedFilmBody));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldAddGenreToFilm() throws Exception {
        FilmDto film = FilmDto.builder()
                .name("name")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .build();
        String filmBody = objectMapper.writeValueAsString(film);
        FilmDto updatedFilm = FilmDto.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .genre(GenreDto.builder().id(1).name("Комедия").build())
                .build();
        String updatedFilmBody = objectMapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(post("/films").content(filmBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        this.mockMvc.perform(put("/films").content(updatedFilmBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedFilmBody));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowNotFoundWhenAddingWrongGenre() throws Exception {
        FilmDto film = FilmDto.builder()
                .name("name")
                .description("description")
                .releaseDate(TEST_DATE)
                .duration(1)
                .mpa(MpaDto.builder().id(1).name("G").build())
                .director(DirectorDto.builder().id(1).name("Director 1").build())
                .genre(GenreDto.builder().id(99).build())
                .build();
        String filmBody = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(filmBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldListDirectorFilms() throws Exception {

        this.mockMvc.perform(get("/films/director/1?sortBy=year"))
                .andExpect(status().isOk())
                .andExpect(content().json(FILMS_BY_YEAR));

        this.mockMvc.perform(get("/films/director/1?sortBy=likes"))
                .andExpect(status().isOk())
                .andExpect(content().json(FILMS_BY_LIKES));
    }
}
