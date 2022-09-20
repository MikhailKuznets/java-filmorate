package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getAllFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);
}
