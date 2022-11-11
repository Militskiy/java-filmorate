package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.dto.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.services.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Review services")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get all reviews")
    public ResponseEntity<Collection<ReviewDto>> findAllReviews(
            @RequestParam @Min(1) Optional<Integer> filmId,
            @RequestParam(defaultValue = "10", required = false) @Min(1) Integer count
    ) {
        if (filmId.isPresent()) {
            log.debug("Getting top {} reviews for film with id: {}", count, filmId.get());
            return ResponseEntity.ok(reviewService.findAllReviewsForFilm(filmId.get(), count)
                    .stream()
                    .map(ReviewMapper.INSTANCE::reviewToReviewDto)
                    .collect(Collectors.toList()));
        }
        log.debug("Getting top {} reviews", count);
        return ResponseEntity.ok(reviewService.findTop(count)
                .stream()
                .map(ReviewMapper.INSTANCE::reviewToReviewDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific review by ID")
    public ResponseEntity<ReviewDto> findReview(@PathVariable @Min(1) Integer id) {
        log.debug("Getting review with id: {}", id);
        return ResponseEntity.ok(ReviewMapper.INSTANCE.reviewToReviewDto(reviewService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Add review")
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        log.debug("Creating review for film with id: {}", reviewDto.getFilmId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ReviewMapper.INSTANCE.reviewToReviewDto(
                                reviewService.createReview(ReviewMapper.INSTANCE.reviewDtoToReview(reviewDto))
                        )
                );
    }

    @PutMapping
    @Operation(summary = "Update review")
    public ResponseEntity<ReviewDto> updateReview(@Valid @RequestBody ReviewDto reviewDto) {
        log.debug("Updating review with id: {} for film with id: {}", reviewDto.getReviewId(), reviewDto.getFilmId());
        return ResponseEntity.ok(
                ReviewMapper.INSTANCE.reviewToReviewDto(
                        reviewService.updateReview(ReviewMapper.INSTANCE.reviewDtoToReview(reviewDto))
                )
        );
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Add like to film review")
    public ResponseEntity<ReviewDto> addLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        log.debug("Adding like to review with id: {} from user with id: {}", id, userId);
        return ResponseEntity.ok(ReviewMapper.INSTANCE.reviewToReviewDto(reviewService.addLike(id, userId)));
    }

    @PutMapping("/{id}/dislike/{userId}")
    @Operation(summary = "Add dislike to film review")
    public ResponseEntity<ReviewDto> addDislike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        log.debug("Adding dislike to review with id: {} from user with id: {}", id, userId);
        return ResponseEntity.ok(ReviewMapper.INSTANCE.reviewToReviewDto(reviewService.addDislike(id, userId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete specific review by ID")
    public void deleteReview(@PathVariable @Min(1) Integer id) {
        log.debug("deleting review with id: {}", id);
        reviewService.deleteReview(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Delete like from film review")
    public void deleteLike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        log.debug("Deleting like from review with id: {} by user with id: {}", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    @Operation(summary = "Delete dislike from film review")
    public void deleteDislike(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer userId
    ) {
        log.debug("Deleting dislike from review with id: {} by user with id: {}", id, userId);
        reviewService.deleteDislike(id, userId);
    }
}
