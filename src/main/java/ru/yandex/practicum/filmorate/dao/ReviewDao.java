package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewDao extends Dao<Review> {
    String GET_ALL_REVIEWS =
            "SELECT * FROM REVIEWS ORDER BY USEFUL DESC";

    String GET_ALL_REVIEWS_FOR_FILM =
            "SELECT * FROM REVIEWS WHERE FILM_ID = ? ORDER BY USEFUL DESC LIMIT ?";

    String FIND_REVIEW =
            "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";

    String UPDATE_REVIEW =
            "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ?, USER_ID = ?, FILM_ID = ? WHERE REVIEW_ID = ?";

    String DELETE_REVIEW =
            "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
    

    String ADD_LIKE =
            "INSERT INTO USERS_REVIEWS (USER_ID, REVIEW_ID, IS_LIKE) VALUES ( ?, ?, true );" +
                    "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?";

    String DELETE_LIKE =
            "DELETE FROM USERS_REVIEWS WHERE USER_ID = ? AND REVIEW_ID = ?;" +
                    "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?";
    String ADD_DISLIKE =
            "INSERT INTO USERS_REVIEWS (USER_ID, REVIEW_ID, IS_LIKE) VALUES ( ?, ?, false );" +
                    "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?";

    String DELETE_DISLIKE =
            "DELETE FROM USERS_REVIEWS WHERE USER_ID = ? AND REVIEW_ID = ?;" +
                    "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?";

    String FIND_LIKED_REVIEWS_BY_USER =
            "SELECT * FROM REVIEWS WHERE REVIEW_ID IN " +
                    "(SELECT REVIEW_ID FROM USERS_REVIEWS WHERE USER_ID = ? AND IS_LIKE = TRUE)";
    String FIND_DISLIKED_REVIEWS_BY_USER =
            "SELECT * FROM REVIEWS WHERE REVIEW_ID IN " +
                    "(SELECT REVIEW_ID FROM USERS_REVIEWS WHERE USER_ID = ? AND IS_LIKE = FALSE)";

    Collection<Review> findAllForFilm(Integer filmId, Integer count);

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(Integer id);

    Review addLike(Integer id, Integer userId);
    Review addDislike(Integer id, Integer userId);
    void deleteLike(Integer id, Integer userId);
    void deleteDislike(Integer id, Integer userId);
    Collection<Review> findLikedReviewsByUser(Integer userId);
    Collection<Review> findDislikedReviewsByUser(Integer userId);
}
