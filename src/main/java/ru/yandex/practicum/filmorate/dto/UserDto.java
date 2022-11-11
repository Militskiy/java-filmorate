package ru.yandex.practicum.filmorate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Value
@Builder
@Jacksonized
public class UserDto {
    int id;
    @NotNull
    @Email
    @Schema(type = "string", example = "test@test.com")
    String email;
    @NotBlank(message = "Invalid login")
    @Pattern(regexp = "^\\S*$", message = "Invalid login")
    @Schema(example = "login")
    String login;
    @Schema(example = "name")
    String name;
    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday must be in the past")
    @Schema(example = "2000-10-22")
    LocalDate birthday;
    @Singular
    @Schema(example = "[]")
    Set<UserDto> friends;
}
