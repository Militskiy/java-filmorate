package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class Review {
    int reviewId;
    String content;
    Boolean isPositive;
    Integer userId;
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
