package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    @NotNull
    @Email
    @Schema(type = "string", example = "test@test.com")
    private String email;
    @NotBlank(message = "Invalid login")
    @Pattern(regexp = "^\\S*$", message = "Invalid login")
    @Schema(example = "login")
    private String login;
    @Schema(example = "name")
    private String name;
    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday must be in the past")
    @Schema(example = "2000-10-22")
    private LocalDate birthday;
    @Schema(example = "[]")
    private final Set<User> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public boolean addFriend(User user) {
        return friends.add(user);
    }
}
