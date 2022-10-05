package ru.yandex.practicum.filmorate.exceptions;

import java.util.NoSuchElementException;

public class NoSuchGenreException extends NoSuchElementException {
    public NoSuchGenreException(String s) {
        super(s);
    }
}
