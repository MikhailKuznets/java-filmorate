package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T> {

    T create(T data);

    T update(T data);


    Optional<T> findById(Integer id);

    Collection<T> getAll();
}
