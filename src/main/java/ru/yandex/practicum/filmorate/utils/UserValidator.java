package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public final class UserValidator {

    private UserValidator() {
    }

    public static void validateUser(User user) {

        if (user.getEmail() == null) {
            throw new ValidationException("Invalid email address");
        }
        if (!user.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            throw new ValidationException("Invalid email address");
        }
        if (user.getLogin() == null) {
            throw new ValidationException("Invalid login");
        }
        if (user.getLogin().split(" ").length > 1 || user.getLogin().isBlank()) {
            throw new ValidationException("Invalid login");
        }
        if (user.getBirthday() == null) {
            throw new ValidationException("Invalid birthday");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday must be in the past");
        }
    }
}
