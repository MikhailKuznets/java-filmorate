package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Получен POST - запрос к /films, переданное значение FILM = {}", film);
        Film createdFilm = service.create(film);
        log.info("Фильму: {}, Присвоен Id = {}", createdFilm.getName(), createdFilm.getId());
        return createdFilm;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody final Film film) {
        log.info("Получен PUT - запрос к /films, переданное значение FILM = {}", film);
        return service.update(film);
    }

    @GetMapping("/films")
    public Collection<Film> getAll() {
        final List<Film> films = service.getAll();
        log.info("Get all films {}", films.size());
        return films;
    }

    @GetMapping("/films/{id}")
    public Film get(@PathVariable Long id) {
        log.info("Get film id: {}", id);
        return service.get(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Add like film id: {} from user: {}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Remove like film id: {} from user: {}", id, userId);
        service.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Get popular films count: {}", count);
        return service.getPopular(count);
    }
}
