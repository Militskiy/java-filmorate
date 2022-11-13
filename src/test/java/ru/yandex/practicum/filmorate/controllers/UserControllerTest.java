package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

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
public class UserControllerTest implements TestJsons {

    private final static LocalDate BIRTHDAY = LocalDate.now().minusDays(1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldFindAllUsers() throws Exception {
        this.mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(USERS));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql"})
    void givenNewUserWithNullName_whenCreated_thenAddsUserWithNameEqualsLogin() throws Exception {
        //given

        String body = objectMapper.writeValueAsString(
                UserDto.builder()
                        .email("test@test.com")
                        .login("login")
                        .birthday(BIRTHDAY)
                        .build());

        //when
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.name").value("login"))
                .andExpect(jsonPath("$.birthday").value(BIRTHDAY.toString()));
    }

    @Test
    void tryToCreateUserWithNullEmailBadRequest() throws Exception {
        User user = User.builder()
                .login("login")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithInvalidEmailBadRequest() throws Exception {
        User user = User.builder()
                .email("bad email")
                .login("login")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithNullLoginBadRequest() throws Exception {
        User user = User.builder()
                .email("email@email.com")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
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
        User user = User.builder()
                .email("aa@bb.com")
                .login("log in")
                .name("name")
                .birthday(BIRTHDAY)
                .build();

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
        User user = User.builder()
                .email("email@email.com")
                .login("")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithBlankLoginBadRequest() throws Exception {
        User user = User.builder()
                .email("email@email.com")
                .login(" ")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithNullBirthdayBadRequest() throws Exception {
        User user = User.builder()
                .email("email@email.com")
                .login("login")
                .name("name")
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToCreateUserWithFutureBirthdayBadRequest() throws Exception {
        User user = User.builder()
                .email("email@email.com")
                .login("login")
                .name("name")
                .birthday(BIRTHDAY.plusDays(2))
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void tryToUpdateUserWithWrongIdNotFound() throws Exception {
        User user = User.builder()
                .id(10)
                .email("email@email.com")
                .login("login")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof NoSuchUserException))
                .andExpect(result -> assertEquals("No user with such ID: 10",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void createsAndUpdatesUser() throws Exception {
        UserDto user = UserDto.builder()
                .email("email@email.com")
                .login("login")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String userBody = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(userBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        UserDto updatedUser = UserDto.builder()
                .id(4)
                .email("updated@email.com")
                .login("login")
                .name("name")
                .birthday(BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void user3shouldFriendUser1ThenShouldFindUser1AsFriend() throws Exception {
        this.mockMvc.perform(put("/users/3/friends/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/3/friends"))
                .andExpect(status().isOk())
                .andExpect(content().json(FRIEND));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void user2shouldUnfriendUser1ThenShouldFindNoFriends() throws Exception {
        this.mockMvc.perform(delete("/users/2/friends/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/2/friends"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void user2shouldFriendUser3ThenShouldFindCommonFriend3WithUser1() throws Exception {
        this.mockMvc.perform(put("/users/2/friends/3"))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/users/3/friends/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/2/friends/common/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":3," +
                                "\"email\":\"email3@test.com\"," +
                                "\"login\":\"login3\"," +
                                "\"name\":\"name3\"," +
                                "\"birthday\":\"2022-10-01\"," +
                                "\"friends\":[{" +
                                "\"id\":1," +
                                "\"email\":\"email1@test.com\"," +
                                "\"login\":\"login1\"," +
                                "\"name\":\"name1\"," +
                                "\"birthday\":\"2022-10-01\"," +
                                "\"friends\":[]}]}]")
                );
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void whenAddingFriendThatIsAlreadyFriendShouldThrowBadArgument() throws Exception {
        this.mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void whenAddingFriendThatIsUnknownShouldThrowNoSuchUser() throws Exception {
        this.mockMvc.perform(put("/users/1/friends/5"))
                .andExpect(status().isNotFound());
        this.mockMvc.perform(put("/users/5/friends/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void tryToAddSelfAsFriend() throws Exception {
        this.mockMvc.perform(put("/users/1/friends/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldGetAddFriendEvent() throws Exception {
        this.mockMvc.perform(put("/users/2/friends/3"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/2/feed"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{" +
                        "\"eventId\":1," +
                        "\"userId\":2," +
                        "\"eventType\":\"FRIEND\"," +
                        "\"operation\":\"ADD\"," +
                        "\"entityId\":3" +
                        "}" +
                        "]"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/restart.sql", "file:assets/scripts/recommendation_test.sql"})
    void shouldGetRecommendationForFilm3() throws Exception {
        this.mockMvc.perform(get("/users/3/recommendations"))
                .andExpect(status().isOk())
                .andExpect(content().json(RECOMMENDATIONS));
    }
}
