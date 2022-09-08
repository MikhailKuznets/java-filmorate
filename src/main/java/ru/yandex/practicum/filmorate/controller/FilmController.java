package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
        log.info("Получен POST - запрос к /films, переданное значение FILM = {}", film);
        film.setId(currentFilmId);
        films.put(currentFilmId, film);
        log.info("Фильму: {}, Присвоен Id = {}", film.getName(), film.getId());
        increaseCurrentFilmId();
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен PUT - запрос к /films, переданное значение FILM = {}", film);
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new IdValidationException("Фильм с Id: " + id + " отсутствует в базе");
        }

    }


}
