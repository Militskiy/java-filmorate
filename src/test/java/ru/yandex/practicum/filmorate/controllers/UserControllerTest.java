package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserControllerTest {

    private final static LocalDate BIRTHDAY = LocalDate.now().minusDays(1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;


    @Transactional
    @Test
    void shouldFindAllUsers() throws Exception {
        User user1 = new User("test@test.com", "test", "", BIRTHDAY);
        User user2 = new User("test@test.com", "test", null, BIRTHDAY);
        User user3 = new User("test@test.com", "test", " ", BIRTHDAY);
        User user4 = new User("test@test.com", "test", "name", BIRTHDAY);
        userController.createUser(user1);
        userController.createUser(user2);
        userController.createUser(user3);
        userController.createUser(user4);
        String body = objectMapper.writeValueAsString(List.of(user1, user2, user3, user4));
        this.mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Transactional
    @Test
    void givenNewUserWithNullName_whenCreated_thenAddsUserWithNameEqualsLogin() throws Exception {
        //given
        String body = objectMapper.writeValueAsString(
                new User("test@test.com", "login", null, BIRTHDAY));

        //when
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.name").value("login"))
                .andExpect(jsonPath("$.birthday").value(BIRTHDAY.toString()));
    }

    @Test
    void tryToCreateUserWithNullEmailBadRequest() throws Exception {
        User user = new User(null, "login", "name", BIRTHDAY);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
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
    void givenUserWithSpaceInLogin_whenPosting_thenThrowsValidationException() throws Exception {
        //given
        User user = new User("aa@bb.com", "log in", "name", BIRTHDAY);
        String requestBody = objectMapper.writeValueAsString(user);

        //when
        this.mockMvc.perform(post("/users")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
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
                .andExpect(result -> assertEquals("No user with such ID: 10",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Transactional
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
