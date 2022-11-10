package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchDirectorException;
import ru.yandex.practicum.filmorate.model.Director;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void getDirectors() throws Exception {
        mockMvc.perform(
                        get("/directors"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Director 1\"}]"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void getDirector() throws Exception {
        mockMvc.perform(
                        get("/directors/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Director 1\"}"));
    }

    @Test
    void getDirectorFailNotFound() throws Exception {

        mockMvc.perform(
                        get("/directors/-1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchDirectorException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void createDirector() throws Exception {

        DirectorDto director = DirectorDto.builder().name("Director 1").build();
        mockMvc.perform(
                        post("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Director 1"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void updateDirector() throws Exception {

        DirectorDto director = DirectorDto.builder().id(1).name("Director 2").build();

        mockMvc.perform(
                        put("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(director.getId()))
                .andExpect(jsonPath("$.name").value("Director 2"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void updateDirectorFailNotFound() throws Exception {

        Director director = new Director(1, "Director 1");
        mockMvc.perform(
                        put("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchDirectorException));

    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void deleteDirector() throws Exception {
        mockMvc.perform(
                        delete("/directors/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(
                get("/directors"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void deleteDirectorFailNotFound() throws Exception {

        Director director = new Director(1, "Director 1");
        mockMvc.perform(
                        delete("/directors/-1")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchDirectorException));

    }
}