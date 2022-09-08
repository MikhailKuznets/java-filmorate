package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int currentFilmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private void increaseCurrentFilmId() {
        this.currentFilmId++;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(currentFilmId);
        films.put(currentFilmId, film);
        increaseCurrentFilmId();
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Фильм с Id: " + id + " отсутствует в базе");
        }

    }


}
