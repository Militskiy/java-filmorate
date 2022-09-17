package ru.yandex.practicum.filmorate.exceptions;

import java.util.NoSuchElementException;

public class NoSuchFilmException extends NoSuchElementException {
    public NoSuchFilmException(String message) {
        super(message);
    }
}
