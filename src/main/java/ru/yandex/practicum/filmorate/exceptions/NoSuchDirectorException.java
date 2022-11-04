package ru.yandex.practicum.filmorate.exceptions;

import java.util.NoSuchElementException;

public class NoSuchDirectorException extends NoSuchElementException {
    public NoSuchDirectorException(String s) {
        super(s);
    }
}