package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Value
@Builder(setterPrefix = "with")
@Jacksonized
public class Review {
    int reviewId;
    @NotNull
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

    @JsonIgnore
    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("content", this.content);
        parameters.put("is_positive", this.isPositive);
        parameters.put("user_id", this.userId);
        parameters.put("film_id", this.filmId);
        parameters.put("useful", this.useful);
        return parameters;
    }
}
