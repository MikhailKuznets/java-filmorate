package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {

    @Autowired
    public UserService(Storage<User> storage) {
        this.storage = storage;
    }

    @Override
    protected void validate(User data) {
        if (data.getLogin() == null || data.getLogin().isBlank() ||
                data.getLogin().isEmpty() || data.getLogin().contains(" ")) {
            throw new ValidationException("User login invalid");
        }
        if (data.getEmail() == null || data.getEmail().isEmpty() ||
                data.getEmail().isBlank() || !data.getEmail().contains("@")) {
            throw new ValidationException("User Email invalid");
        }
        if (data.getName() == null || data.getName().isEmpty()
                || data.getName().isBlank()) {
            data.setName(data.getLogin());
        }
        if (data.getBirthday() != null && data.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User Birthday invalid");
        }
    }


    public void addFriend(Long userId, Long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

}
