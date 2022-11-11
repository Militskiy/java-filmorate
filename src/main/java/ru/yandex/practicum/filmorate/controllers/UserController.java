package ru.yandex.practicum.filmorate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.mappers.EventMapper;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User services")
public class UserController {

    private final UserService userService;


    @GetMapping
    @Operation(summary = "Get a list of all users")
    public ResponseEntity<Collection<UserDto>> findAllUsers() {
        log.debug("Sending user list");
        return ResponseEntity.ok(
                userService.findAllUsers()
                        .stream()
                        .map(UserMapper.INSTANCE::userToUserDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by its id")
    public ResponseEntity<UserDto> findUser(@PathVariable @Min(1) Integer id) {
        log.debug("Getting user with id: {}", id);
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserDto(userService.findUserById(id)));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user by id")
    public void deleteUser(@PathVariable Integer userId) {
        log.debug("Deleting user with id: " + userId);
        userService.removeUser(userId);
    }

    @GetMapping("/{id}/friends")
    @Operation(summary = "Get a list of specific user friends")
    public ResponseEntity<Collection<UserDto>> findFriends(@PathVariable @Min(1) Integer id) {
        log.debug("Getting friend list for user with id: {}", id);
        return ResponseEntity.ok(
                userService.findFriends(id)
                        .stream()
                        .map(UserMapper.INSTANCE::userToUserDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Operation(summary = "Get a list of user's common friends with another user")
    public ResponseEntity<Collection<UserDto>> findCommonFriends(
            @PathVariable @Min(1) Integer id,
            @PathVariable @Min(1) Integer otherId
    ) {
        log.debug("Getting common friends for users with ids {} and {}", id, otherId);
        return ResponseEntity.ok(
                userService.findCommonFriends(id, otherId)
                        .stream()
                        .map(UserMapper.INSTANCE::userToUserDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/feed")
    @Operation(summary = "Get user event feed")
    public ResponseEntity<Collection<EventDto>> findUserFeed(
            @PathVariable @Min(1) Integer id
    ) {
        log.debug("Getting feed for user with id: {}", id);
        return ResponseEntity.ok(
                userService.findFeed(id)
                        .stream()
                        .map(EventMapper.INSTANCE::eventToEventDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    @Operation(summary = "Creates a user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Creating new user: {}", userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.INSTANCE.userToUserDto(
                        userService.createUser(UserMapper.INSTANCE.userDtoToUser(userDto))
                ));
    }

    @PutMapping
    @Operation(summary = "Updates a user")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Updating user with ID: {}", userDto.getId());
        return ResponseEntity.ok(
                UserMapper.INSTANCE.userToUserDto(userService.updateUser(UserMapper.INSTANCE.userDtoToUser(userDto)))
        );
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
    @Operation(summary = "Get a list of film recommendations to watch")
    public ResponseEntity<Collection<FilmDto>> getRecommendations(@PathVariable Integer id) {
        log.debug("Getting a list of film recommendations for user with id: {}", id);
        return ResponseEntity.ok(
                userService.getRecommendations(id)
                        .stream()
                        .map(FilmMapper.INSTANCE::filmToFilmDto)
                        .collect(Collectors.toList())
        );
    }
}
