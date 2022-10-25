package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmDao extends Dao<Film> {

    void addLike(Integer id, Integer userId);

    void removeLike(Integer id, Integer userId);
}
