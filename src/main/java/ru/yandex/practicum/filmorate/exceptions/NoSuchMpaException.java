package ru.yandex.practicum.filmorate.exceptions;

import java.util.NoSuchElementException;

public class NoSuchMpaException extends NoSuchElementException {
    public NoSuchMpaException(String s) {
        super(s);
    }
}
