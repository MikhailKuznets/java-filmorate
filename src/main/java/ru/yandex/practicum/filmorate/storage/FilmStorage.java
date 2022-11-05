package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage extends Storage<Film> {
    Film create(Film film);

    Film update(Film data);


}
