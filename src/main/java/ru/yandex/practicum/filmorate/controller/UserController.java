package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int currentUserId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    private void increaseCurrentUserId() {
        this.currentUserId++;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен POST - запрос к /users, переданное значение USER = {}", user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(currentUserId);
        users.put(currentUserId, user);
        log.info("Пользователю: {}, Присвоен Id = {}", user.getName(), user.getId());
        increaseCurrentUserId();
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT - запрос к /users, переданное значение USER = {}", user);
        int id = user.getId();
        if (users.containsKey(id)) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new IdValidationException("User с Id: " + id + " отсутствует в базе");
        }
    }

}
