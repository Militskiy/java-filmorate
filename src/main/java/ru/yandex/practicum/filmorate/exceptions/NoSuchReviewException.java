package ru.yandex.practicum.filmorate.exceptions;

import java.util.NoSuchElementException;

public class NoSuchReviewException extends NoSuchElementException {
    public NoSuchReviewException(String message) {
        super(message);
    }
}
