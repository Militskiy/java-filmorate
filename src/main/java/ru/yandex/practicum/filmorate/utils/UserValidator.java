package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.exceptions.BadArgumentsException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public final class UserValidator {

    private UserValidator() {
    }

    public static void validateUser(User user) {

        if (user.getEmail() == null ||
                !user.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            throw new BadArgumentsException("Invalid email address");
        }
        if (user.getLogin() == null || user.getLogin().split(" ").length > 1 || user.getLogin().isBlank()) {
            throw new BadArgumentsException("Invalid login");
        }
        if (user.getBirthday() == null) {
            throw new BadArgumentsException("Invalid birthday");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new BadArgumentsException("Birthday must be in the past");
        }
    }
}
