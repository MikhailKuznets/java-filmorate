package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
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
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(currentUserId);
        users.put(currentUserId, user);
        increaseCurrentUserId();
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        int id = user.getId();
        if (users.containsKey(id)) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("User с Id: " + id + " отсутствует в базе");
        }
    }

}
