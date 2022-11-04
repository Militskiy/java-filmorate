package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder(setterPrefix = "with")
@Jacksonized
public class Review {
    private int reviewId;
    @NotNull
    @Schema(type = "string", example = "A good review")
    private String content;
    @NotNull
    @Schema(type = "boolean", example = "true")
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;
    @NotNull
    @Schema(type = "integer", example = "1")
    private Integer userId;
    @NotNull
    @Schema(type = "integer", example = "1")
    private Integer filmId;
    private int useful;

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
