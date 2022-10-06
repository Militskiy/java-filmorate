package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String GENRES =
            "[{\"id\":1,\"name\":\"Комедия\"}," +
            "{\"id\":2,\"name\":\"Драма\"}," +
            "{\"id\":3,\"name\":\"Мультфильм\"}," +
            "{\"id\":4,\"name\":\"Триллер\"}," +
            "{\"id\":5,\"name\":\"Документальный\"}," +
            "{\"id\":6,\"name\":\"Боевик\"}]";

    @Test
    void findAll() throws Exception {
        this.mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(GENRES));
    }

    @Test
    void findGenre() throws Exception {
        this.mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Комедия"));
    }

    @Test
    void findGenreWrongId() throws Exception {
        this.mockMvc.perform(get("/genres/10"))
                .andExpect(status().isNotFound());
    }
}