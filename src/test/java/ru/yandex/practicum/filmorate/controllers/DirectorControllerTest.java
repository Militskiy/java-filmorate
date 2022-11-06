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
import ru.yandex.practicum.filmorate.exceptions.NoSuchDirectorException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Autowired
    private DirectorController directorController;

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void getDirectors() throws Exception {

        Director director1 = directorController.createDirector(
                new Director("Director 1"));
        Director director2 = directorController.createDirector(
                new Director("Director 2"));
        String list = objectMapper.writeValueAsString(List.of(director1, director2));

        mockMvc.perform(
                        get("/directors"))
                .andExpect(status().isOk())
                .andExpect(content().json(list));
    }

    @Test
    void getDirector() throws Exception {
        Director director = directorController.createDirector(
                new Director("Director 1"));

        String json = objectMapper.writeValueAsString(director);

        mockMvc.perform(
                        get("/directors/" + director.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
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

        Director director = new Director("Director 1");
        mockMvc.perform(
                        post("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Director 1"));

        Director result = directorController.getDirector(1);
        assertEquals(director.getName(), result.getName());
    }

    @Test
    void updateDirector() throws Exception {

        Director director = directorController.createDirector(new Director("Director 1"));

        director.setName("Director 2");
        mockMvc.perform(
                        put("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(director.getId()))
                .andExpect(jsonPath("$.name").value("Director 2"));

        Director result = directorController.getDirector(director.getId());

        assertEquals(director.getName(), result.getName());

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
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void deleteDirector() throws Exception {
        Director director1 = directorController.createDirector(
                new Director("Director 1"));
        Director director2 = directorController.createDirector(
                new Director("Director 2"));

        mockMvc.perform(
                        delete("/directors/" + director2.getId()))
                .andExpect(status().isOk());


        List<Director> result = directorController.getDirectors();
        assertEquals(1, result.size());
        assertEquals(director1.getId(), result.get(0).getId());
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