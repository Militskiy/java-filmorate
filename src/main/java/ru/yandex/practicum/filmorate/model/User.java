package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private int id;
    private String email;
    private String login;

    private String name;
    private LocalDate birthday;
    @Singular
    private final Set<User> friends = new HashSet<>();

    public void addFriend(User user) {
        friends.add(user);
    }

    @JsonIgnore
    public Map<String, Object> getMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", this.id);
        parameters.put("user_email", this.email);
        parameters.put("user_login", this.login);
        parameters.put("user_name", this.name);
        parameters.put("birthday", this.birthday);
        return parameters;
    }
}
