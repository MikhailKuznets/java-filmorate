package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {
}
