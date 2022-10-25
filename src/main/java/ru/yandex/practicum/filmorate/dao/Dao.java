package ru.yandex.practicum.filmorate.dao;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Integer id);

    create

    Collection<T> getAll();
}
