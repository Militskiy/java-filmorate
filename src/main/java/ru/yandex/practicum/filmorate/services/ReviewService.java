package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDao reviewStorage;

    public Collection<Review> findAllReviews() {
        return reviewStorage.findAll();
    }

    public Collection<Review> findAllReviewsForFilm(Integer filmId, Integer count) {
        return reviewStorage.findAllForFilm(filmId, count);
    }

    public Review findById(Integer reviewId) {
        return reviewStorage.findById(reviewId);
    }

    public Review createReview(Review review) {
        return reviewStorage.createReview(review);
    }

    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(Integer id) {
        reviewStorage.deleteReview(id);
    }

    public Review addLike(Integer id, Integer userId) {
        Review review = reviewStorage.findById(id);
        if (review.getUserId() != userId.intValue()) {
            if (reviewStorage.findDislikedReviewsByUser(userId).stream().anyMatch(r -> r.getReviewId() == id)) {
                reviewStorage.deleteDislike(id, userId);
            }
        } else {
            throw new BadArgumentsException("Cannot like own review");
        }
        return reviewStorage.addLike(id, userId);
    }

    public Review addDislike(Integer id, Integer userId) {
        Review review = reviewStorage.findById(id);
        if (review.getUserId() != userId.intValue()) {
            if (reviewStorage.findLikedReviewsByUser(userId).stream().anyMatch(r -> r.getReviewId() == id)) {
                reviewStorage.deleteLike(id, userId);
            }
        } else {
            throw new BadArgumentsException("Cannot dislike own review");
        }
        return reviewStorage.addDislike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        Review review = reviewStorage.findById(id);
        if (review.getUserId() != userId.intValue()) {
            if (reviewStorage.findLikedReviewsByUser(userId).stream().anyMatch(r -> r.getReviewId() == id)) {
                reviewStorage.deleteLike(id, userId);
            } else {
                throw new NoSuchElementException("User didnt like this review");
            }
        } else {
            throw new BadArgumentsException("Cannot delete like from your own review");
        }
    }

    public void deleteDislike(Integer id, Integer userId) {
        Review review = reviewStorage.findById(id);
        if (review.getUserId() != userId.intValue()) {
            if (reviewStorage.findDislikedReviewsByUser(userId).stream().anyMatch(r -> r.getReviewId() == id)) {
                reviewStorage.deleteDislike(id, userId);
            } else {
                throw new NoSuchElementException("User didnt dislike this review");
            }
        } else {
            throw new BadArgumentsException("Cannot delete dislike from your own review");
        }
    }
}
