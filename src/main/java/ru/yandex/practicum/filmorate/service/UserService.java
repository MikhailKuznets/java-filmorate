package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;


    protected void validate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() ||
                user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("User login invalid");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("User Email invalid");
        }
        if (user.getName() == null || user.getName().isEmpty()
                || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User Birthday invalid");
        }
    }


    public void addFriend(Long userId, Long friendId) {
        final User user = userStorage.get(userId);
        final User friend = userStorage.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        friendStorage.addFriend(user.getId(), friend.getId());
    }

    public void removeFriend(Long userId, Long friendId) {
        final User user = userStorage.get(userId);
        final User friend = userStorage.get(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
        friendStorage.removeFriend(user.getId(), friend.getId());
    }

    public List<User> getFriends(Long userId) {
        final User user = userStorage.get(userId);
        return friendStorage.getFriends(user.getId());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> result = new ArrayList<>(userStorage.getCommonFriends(id, otherId));
        if(result.get(0).getId() == id || result.get(1).getId() == id) {
            if (result.get(1).getId() == otherId || result.get(0).getId() == otherId) {
                log.debug("Listing common friends for users with ids {} and {}", id, otherId);
                return result.stream().skip(2).collect(Collectors.toList());
            } else {
                throw new DataNotFoundException("No user with such ID: " + otherId);
            }
        } else {
            throw new DataNotFoundException("No user with such ID: " + id);
        }
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        if (userStorage.update(user) == 1) {
            log.debug("Обновление данных пользователя с ID = {}", user.getId());
            return user;
        } else {
            throw new DataNotFoundException("Отсутствует пользователь с ID = " + user.getId());
        }
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User get(Long id) {
        return userStorage.get(id);
    }
}
