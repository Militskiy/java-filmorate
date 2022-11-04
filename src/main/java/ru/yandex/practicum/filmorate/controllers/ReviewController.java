package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.services.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Review services")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get all reviews")
    public Collection<Review> findAllReviews(
            @RequestParam @Min(1) Optional<Integer> filmId,
            @RequestParam(defaultValue = "10", required = false) @Min(1) Integer count
            ){
        if (filmId.isPresent()) {
            return reviewService.findAllReviewsForFilm(filmId.get(), count);
        }
        return reviewService.findAllReviews();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific review by ID")
    public Review findReview(@PathVariable @Min(1) Integer id) {
        return reviewService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add review")
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping
    @Operation(summary = "Update review")
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Add like to film review")
    public Review addLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        return reviewService.addLike(id, userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete specific review by ID")
    public void deleteReview(@PathVariable @Min(1) Integer id) {
        reviewService.deleteReview(id);
    }
 }
