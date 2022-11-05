package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends Storage<User> {
    User create(User user);

    int update(User user);

    Collection<User> getCommonFriends(Long id, Long otherId);

}
