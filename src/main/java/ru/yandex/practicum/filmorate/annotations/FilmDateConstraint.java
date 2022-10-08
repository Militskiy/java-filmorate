package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validators.FilmDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FilmDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmDateConstraint {
    String message() default "Film release date must be after 28.12.1985";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
