package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    static User user;
    private final static LocalDate BIRTHDAY = LocalDate.now().minusDays(1);
    UserController userController;

    @BeforeAll
    static void beforeAll() {
        user = new User("test@test.com", "test", "Alex", BIRTHDAY);
    }

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void findAllUsers() {
        userController.createUser(user);
        userController.createUser(new User("test@test.com", "test", "Alex", BIRTHDAY));
        userController.createUser(new User("test@test.com", "test", "Alex", BIRTHDAY));
        userController.updateUser(new User(1, "test@test.com", "updatedLogin", "Alex", BIRTHDAY));
        assertEquals(3, userController.findAllUsers().size());
    }

    @Test
    void throwsValidationExceptionInvalidEmail() {
        User invalidEmailUser = new User("это-неправильный?эмейл@", "test", "Alex", BIRTHDAY);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> userController.createUser(invalidEmailUser));
        assertEquals("Invalid email address", ex.getMessage());

        User nullEmailUser = new User(null, "test", "Alex", BIRTHDAY);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(nullEmailUser));
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void throwsValidationExceptionInvalidLogin() {
        User nullLoginUser = new User("test@test.com", null, "Alex", BIRTHDAY);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> userController.createUser(nullLoginUser));
        assertEquals("Invalid login", ex.getMessage());

        User invalidLoginUser = new User("test@test.com", " ", "Alex", BIRTHDAY);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(invalidLoginUser));
        assertEquals("Invalid login", exception.getMessage());

        User spacesInLoginUser = new User("test@test.com", "test test test", "Alex", BIRTHDAY);
        ValidationException e = assertThrows(ValidationException.class,
                () -> userController.createUser(spacesInLoginUser));
        assertEquals("Invalid login", e.getMessage());
    }

    @Test
    void throwsValidationExceptionInvalidBirthdate() {
        User nullBirthdateUser = new User("test@test.com", "test", "Alex", null);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> userController.createUser(nullBirthdateUser));
        assertEquals("Invalid birthday", ex.getMessage());

        User futureBirthdateUser =
                new User("test@test.com", "test", "Alex", LocalDate.now().plusDays(1));
        ValidationException e = assertThrows(ValidationException.class,
                () -> userController.createUser(futureBirthdateUser));
        assertEquals("Birthday must be in the past", e.getMessage());
    }
}