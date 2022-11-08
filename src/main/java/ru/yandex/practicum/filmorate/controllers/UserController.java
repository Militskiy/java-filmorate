package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User services")
public class UserController {

    private final UserService userService;


    @GetMapping
    @Operation(summary = "Get a list of all users")
    public Collection<User> findAllUsers() {
        log.debug("Sending user list");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by its id")
    public User findUser(@PathVariable @Min(1) Integer id) {
        log.debug("Getting user with id: {}", id);
        return userService.findUserById(id);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user by id")
    public void deleteUser(@PathVariable Integer userId) {
        log.debug("Deleting user with id: " + userId);
        userService.removeUser(userId);
    }

    @GetMapping("/{id}/friends")
    @Operation(summary = "Get a list of specific user friends")
    public Collection<User> findFriends(@PathVariable @Min(1) Integer id) {
        log.debug("Getting friend list for user with id: {}", id);
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Operation(summary = "Get a list of user's common friends with another user")
    public Collection<User> findCommonFriends(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer otherId
    ) {
        log.debug("Getting common friends for users with ids {} and {}", id, otherId);
        return userService.findCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/feed")
    @Operation(summary = "Get user event feed")
    public Collection<Event> findUserFeed(
            @PathVariable @Min(1) Integer id
    ) {
        log.debug("Getting feed for user with id: {}", id);
        return userService.findFeed(id);
    }

    @PostMapping
    @Operation(summary = "Creates a user")
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Creating new user: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    @Operation(summary = "Updates a user")
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Updating user with ID: {}", user.getId());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Adds a friend to specific user")
    public void addFriend(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer friendId
    ) {
        log.debug("Creating friend link between users with ids: {} and {}", id, friendId);
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Deletes a friend from specific user")
    public void deleteFriend(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer friendId
    ) {
        log.debug("Deleting friend link between users with ids: {} and {}", id, friendId);
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/recommendations")
    @Operation(summary = "Recommendations for films to watch")
    public List<Film> getRecommendations(@PathVariable Integer id) {
        log.debug("Getting film recommendations for user with id: {}", id);
        return userService.getRecommendations(id);
    }
}
