package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("Получен POST - запрос к /users, переданное значение USER = {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        log.info("Получен PUT - запрос к /users, переданное значение USER = {}", user);
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен Get - запрос к /users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Получен Get - запрос к /users/id , id = {}", id);
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен Put - запрос к /users/{id}/friends/{friendId} , id = {}, friendId = {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен Delete - запрос к /users/{id}/friends/{friendId} , id = {}, friendId = {}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Получен Get - запрос к /users/{id}/friends , id = {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен Get - запрос к /users/{id}/friends/common/{otherId} , id = {}, otherId = {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}
