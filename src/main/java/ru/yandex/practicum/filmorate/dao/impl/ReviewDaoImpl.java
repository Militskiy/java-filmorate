package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchReviewException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    private final FilmDao filmStorage;
    private final UserDao userStorage;
    private final EventDao eventStorage;
    @Override
    public Collection<Review> findAll() {
        return jdbcTemplate.query(GET_ALL_REVIEWS, this::makeReview);
    }

    @Override
    public Collection<Review> findTop(Integer count) {
        return jdbcTemplate.query(GET_TOP_REVIEWS, this::makeReview, count);
    }

    @Override
    public Collection<Review> findAllForFilm(Integer filmId, Integer count) {
        filmStorage.findById(filmId);
        return jdbcTemplate.query(GET_ALL_REVIEWS_FOR_FILM, this::makeReview, filmId, count);
    }

    @Override
    public Review findById(Integer id) {
        return findOptionalReview(id).orElseThrow(() -> new NoSuchReviewException("No review with such ID: " + id));
    }

    @Override
    public Review createReview(Review review) {
        filmStorage.findById(review.getFilmId());
        userStorage.findById(review.getUserId());
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("reviews")
                    .usingGeneratedKeyColumns("review_id");
            int id = simpleJdbcInsert.executeAndReturnKey(review.toMap()).intValue();
            eventStorage.createEvent(review.getUserId(), EventType.REVIEW, Operation.ADD, id);
            return findById(id);
        } catch (DuplicateKeyException e) {
            throw new BadArgumentsException("User already reviewed this film");
        }
    }

    @Override
    public Review updateReview(Review review) {
        Review originalReview = findById(review.getReviewId());
        jdbcTemplate.update(
                UPDATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        eventStorage.createEvent(originalReview.getUserId(), EventType.REVIEW, Operation.UPDATE, review.getReviewId());
        return findById(review.getReviewId());
    }

    @Override
    public void deleteReview(Integer id) {
        Review review = findById(id);
        eventStorage.createEvent(review.getUserId(), EventType.REVIEW, Operation.REMOVE, id);
        jdbcTemplate.update(DELETE_REVIEW, id);
    }

    @Override
    public Review addLike(Integer id, Integer userId) {
        userStorage.findById(userId);
        try {
            jdbcTemplate.update(ADD_LIKE, userId, id, id);
            return findById(id);
        } catch (DuplicateKeyException e) {
            throw new BadArgumentsException("Review already liked");
        }
    }

    @Override
    public Review addDislike(Integer id, Integer userId) {
        userStorage.findById(userId);
        try {
            jdbcTemplate.update(ADD_DISLIKE, userId, id, id);
            return findById(id);
        } catch (DataAccessException e) {
            throw new BadArgumentsException("Review already disliked");
        }
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        userStorage.findById(userId);
        findById(id);
        jdbcTemplate.update(DELETE_LIKE, userId, id, id);
    }

    @Override
    public void deleteDislike(Integer id, Integer userId) {
        userStorage.findById(userId);
        findById(id);
        jdbcTemplate.update(DELETE_DISLIKE, userId, id, id);
    }

    @Override
    public Collection<Review> findLikedReviewsByUser(Integer userId) {
        userStorage.findById(userId);
        return jdbcTemplate.query(FIND_LIKED_REVIEWS_BY_USER, this::makeReview, userId);
    }

    @Override
    public Collection<Review> findDislikedReviewsByUser(Integer userId) {
        userStorage.findById(userId);
        return jdbcTemplate.query(FIND_DISLIKED_REVIEWS_BY_USER, this::makeReview, userId);
    }

    private Optional<Review> findOptionalReview(Integer id) {
        return jdbcTemplate.query(FIND_REVIEW, this::makeReview, id)
                .stream()
                .findAny();
    }

    private Review makeReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }
}
