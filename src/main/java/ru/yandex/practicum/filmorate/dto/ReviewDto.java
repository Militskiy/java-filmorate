package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class ReviewDto {
    int reviewId;
    @NotNull
    @NotBlank
    @Schema(type = "string", example = "A good review")
    String content;
    @NotNull
    @Schema(type = "boolean", example = "true")
    @JsonProperty(value = "isPositive")
    Boolean isPositive;
    @NotNull
    @Schema(type = "integer", example = "1")
    Integer userId;
    @NotNull
    @Schema(type = "integer", example = "1")
    Integer filmId;
    int useful;
}
