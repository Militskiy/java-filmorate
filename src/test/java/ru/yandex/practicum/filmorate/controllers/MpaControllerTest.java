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
class MpaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String MPA =
            "[{\"id\":1,\"name\":\"G\"}," +
                    "{\"id\":2,\"name\":\"PG\"}," +
                    "{\"id\":3,\"name\":\"PG-13\"}," +
                    "{\"id\":4,\"name\":\"R\"}," +
                    "{\"id\":5,\"name\":\"NC-17\"}]";

    @Test
    void findAll() throws Exception {
        this.mockMvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(content().json(MPA));
    }

    @Test
    void findGenre() throws Exception {
        this.mockMvc.perform(get("/mpa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("G"));
    }

    @Test
    void findGenreWrongId() throws Exception {
        this.mockMvc.perform(get("/mpa/10"))
                .andExpect(status().isNotFound());
    }
}