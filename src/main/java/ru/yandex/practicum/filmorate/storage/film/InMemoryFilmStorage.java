package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int currentFilmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private void increaseCurrentFilmId() {
        this.currentFilmId++;
    }

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film createFilm(Film film) {
        film.setId(currentFilmId);
        films.put(currentFilmId, film);
        increaseCurrentFilmId();
        return film;
    }

    public Film updateFilm(Film film) throws IdValidationException {
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new IdValidationException("Фильм с Id: " + id + " отсутствует в базе");
        }
    }
}
