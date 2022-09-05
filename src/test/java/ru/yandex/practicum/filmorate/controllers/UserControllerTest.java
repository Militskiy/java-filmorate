package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        this.mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void tryToCreateUserWithNullEmailBadRequest() throws Exception {
        User user = new User(null, "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("Invalid email address",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void tryToCreateUserWithInvalidEmailBadRequest() throws Exception {
        User user = new User("это-неправильный?эмейл@", "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithNullLoginBadRequest() throws Exception {
        User user = new User("email@email.com", null, "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithEmptyLoginBadRequest() throws Exception {
        User user = new User("email@email.com", "", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithBlankLoginBadRequest() throws Exception {
        User user = new User("email@email.com", " ", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithNullBirthdayBadRequest() throws Exception {
        User user = new User("email@email.com", "login", "name", null);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithFutureBirthdayBadRequest() throws Exception {
        User user = new User("email@email.com", "login", "name", BIRTHDAY.plusDays(2));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToUpdateUserWithWrongIdNotFound() throws Exception {
        User user = new User(10, "email@email.com", "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof NoSuchUserException))
                .andExpect(result -> assertEquals("No user with such ID",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void createsAndUpdatesUser() throws Exception {
        User user = new User("email@email.com", "login", "name", BIRTHDAY);
        userController.createUser(user);
        User updatedUser = new User(user.getId(), "updated@updated.com", "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }
}
