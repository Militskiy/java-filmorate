package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchReviewException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ReviewControllerTest implements TestJsons{

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldFindAll() throws Exception {
        this.mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json(FIND_ALL_REVIEWS));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldFindReviewWithId2ForFilm1() throws Exception {
        this.mockMvc.perform(get("/reviews?filmId=1&count=1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"reviewId\":2," +
                        "\"content\":\"Very bad review\"," +
                        "\"userId\":2,\"filmId\":1," +
                        "\"useful\":99," +
                        "\"isPositive\":false}" +
                        "]"));
    }

    @Test
    void shouldThrowFilmNotFoundException() throws Exception {
        this.mockMvc.perform(get("/reviews?filmId=99"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchFilmException));
    }

    @Test
    void shouldThrowReviewNotFoundException() throws Exception {
        this.mockMvc.perform(get("/reviews/99"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchReviewException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldFindReviewWithId1() throws Exception {
        this.mockMvc.perform(get("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"reviewId\":1," +
                        "\"content\":\"Very good review\"," +
                        "\"userId\":1,\"filmId\":1," +
                        "\"useful\":0," +
                        "\"isPositive\":true" +
                        "}"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldCreateReviewFromUser3ToFilm3() throws Exception {

        final String body = "{\n" +
                "  \"content\": \"Test review\",\n" +
                "  \"userId\": 3,\n" +
                "  \"filmId\": 3,\n" +
                "  \"isPositive\": true\n" +
                "}";
        this.mockMvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(content().json(body));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldUpdateReviewFromUser1ToFilm1ToNegative() throws Exception {

        final String body = "{\n" +
                "  \"reviewId\": 1,\n" +
                "  \"content\": \"A bad test review\",\n" +
                "  \"userId\": 1,\n" +
                "  \"filmId\": 1,\n" +
                "  \"isPositive\": false\n" +
                "}";
        this.mockMvc.perform(put("/reviews").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldDeleteReview1And3ThenFindReview2() throws Exception {

        this.mockMvc.perform(delete("/reviews/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete("/reviews/3"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/reviews"))
                .andExpect(content().json("[" +
                        "{" +
                        "\"reviewId\":2," +
                        "\"content\":\"Very bad review\"," +
                        "\"userId\":2," +
                        "\"filmId\":1," +
                        "\"useful\":99," +
                        "\"isPositive\":false" +
                        "}" +
                        "]"));
    }

    @Test
    void shouldThrowReviewNotFoundExceptionWhenDeletingNonExistentReview() throws Exception {
        this.mockMvc.perform(delete("/reviews/99"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchReviewException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldAddLikeToReview3ThenFindReview3Second() throws Exception {
        this.mockMvc.perform(put("/reviews/3/like/2"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json(FIND_ALL_REVIEWS_AFTER_ADD_LIKE));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowBadArgumentExceptionWhenLikingOwnReview() throws Exception {
        this.mockMvc.perform(put("/reviews/1/like/1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowReviewNotFoundExceptionWhenLikingNonExistentReview() throws Exception {
        this.mockMvc.perform(put("/reviews/99/like/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchReviewException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowUserNotFoundExceptionWhenLikingWithNonExistentUser() throws Exception {
        this.mockMvc.perform(put("/reviews/1/like/99"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchUserException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowBadArgumentExceptionWhenLiking2TimesSameReview() throws Exception {
        this.mockMvc.perform(put("/reviews/3/like/2"))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/reviews/3/like/2"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldDeleteLikeFromReview3() throws Exception {
        this.mockMvc.perform(delete("/reviews/3/like/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/reviews/3"))
                .andExpect(content().json("" +
                        "{" +
                        "\"reviewId\":3," +
                        "\"content\":\"Very good review\"," +
                        "\"userId\":3," +
                        "\"filmId\":1," +
                        "\"useful\":-1," +
                        "\"isPositive\":true}"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldDeleteDislikeFromReview2() throws Exception {
        this.mockMvc.perform(delete("/reviews/2/dislike/1"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/reviews/2"))
                .andExpect(content().json("" +
                        "{" +
                        "\"reviewId\":2," +
                        "\"content\":\"Very bad review\"," +
                        "\"userId\":2," +
                        "\"filmId\":1," +
                        "\"useful\":100," +
                        "\"isPositive\":false}"));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowBadArgumentWhenDeletingUser2LikeFromReview2() throws Exception {
        this.mockMvc.perform(delete("/reviews/2/like/2"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    @Sql(scripts = {"file:assets/scripts/test_setup.sql"})
    void shouldThrowBadArgumentWhenDeletingUser2DislikeFromReview2() throws Exception {
        this.mockMvc.perform(delete("/reviews/2/dislike/2"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException));
    }
}