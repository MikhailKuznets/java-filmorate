package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody final User user) {
        log.info("Получен POST - запрос к /users, переданное значение USER = {}", user);
        User createdUser = service.create(user);
        log.info("Пользователю: {}, Присвоен Id = {}", createdUser.getName(), createdUser.getId());
        return createdUser;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody final User user) {
        log.info("Получен PUT - запрос к /users, переданное значение USER = {}", user);
        return service.update(user);
    }

    @GetMapping("/users")
    public Collection<User> getAll() {
        final List<User> users = service.getAll();
        log.info("Get all users {}", users.size());
        return users;
    }

    @GetMapping("/users/{id}")
    public User get(@PathVariable Long id) {
        log.info("Get user id: {}", id);
        return service.get(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("To user: {} add friend: {}", id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("To user: {} delete friend: {}", id, friendId);
        service.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Get friends from user: {}", id);
        return service.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Get common friends from users: {} and {}", id, otherId);
        return service.getCommonFriends(id, otherId);
    }

}
