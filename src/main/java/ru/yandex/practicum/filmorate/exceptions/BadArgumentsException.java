package ru.yandex.practicum.filmorate.exceptions;

public class BadArgumentsException extends IllegalArgumentException {
    public BadArgumentsException(String message) {
        super(message);
    }
}
