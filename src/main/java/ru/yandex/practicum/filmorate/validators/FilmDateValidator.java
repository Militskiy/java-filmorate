package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.FilmDateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmDateValidator implements ConstraintValidator<FilmDateConstraint, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !localDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
