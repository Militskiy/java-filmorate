package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final static LocalDate BIRTHDAY = LocalDate.now().minusDays(1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private UserController userController;

    @Test
    void shouldFindAllUsers() throws Exception {
        User user1 = new User("test@test.com", "test", "", BIRTHDAY);
        User user2 = new User("test@test.com", "test", null, BIRTHDAY);
        User user3 = new User("test@test.com", "test", " ", BIRTHDAY);
        User user4 = new User("test@test.com", "test", "name", BIRTHDAY);

        String body = objectMapper.writeValueAsString(List.of(user1, user2, user3, user4));
        Mockito.when(userController.findAllUsers()).thenReturn(List.of(user1, user2, user3, user4));
        mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void tryToCreateUserWithNullEmailBadRequest() throws Exception {
        User user = new User(null, "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateUserWithInvalidEmailBadRequest() throws Exception {
        User user = new User("это-неправильный?эмейл@", "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateUserWithNullLoginBadRequest() throws Exception {
        User user = new User("email@email.com", null, "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateUserWithEmptyLoginBadRequest() throws Exception {
        User user = new User("email@email.com", "", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateUserWithBlankLoginBadRequest() throws Exception {
        User user = new User("email@email.com", " ", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateUserWithNullBirthdayBadRequest() throws Exception {
        User user = new User("email@email.com", "login", "name", null);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToCreateUserWithFutureBirthdayBadRequest() throws Exception {
        User user = new User("email@email.com", "login", "name", BIRTHDAY.plusDays(2));
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryToUpdateUserWithWrongIdNotFound() throws Exception {
        User user = new User(10, "email@email.com", "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsAndUpdatesUser() throws Exception {
        User user = new User("email@email.com", "login", "name", BIRTHDAY);
        Mockito.when(userController.createUser(user)).thenReturn(user);
        User updatedUser = new User(user.getId(), "updated@updated.com", "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(
                put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }
}
