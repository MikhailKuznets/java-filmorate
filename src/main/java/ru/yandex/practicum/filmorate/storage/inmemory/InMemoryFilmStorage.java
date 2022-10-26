package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
}
