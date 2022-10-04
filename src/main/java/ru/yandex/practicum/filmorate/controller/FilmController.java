package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @PostMapping("/films")
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Получен POST - запрос к /films, переданное значение FILM = {}", film);
        return service.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody final Film film) {
        log.info("Получен PUT - запрос к /films, переданное значение FILM = {}", film);
        return service.update(film);
    }

    @GetMapping("/films")
    public Collection<Film> getAll() {
        log.info("Получен Get - запрос к /films");
        return service.getAll();
    }

    @GetMapping("/films/{id}")
    public Film get(@PathVariable Long id) {
        log.info("Получен Get - запрос к /films/{id}, id = {}", id);
        return service.get(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен Put - запрос к /films/{id}/like/{userId}, id = {}, userId = {}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен Delete - запрос к /films/{id}/like/{userId}, id = {}, userId = {}", id, userId);
        service.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен Get - запрос к /films/popular , count = {}", count);
        return service.getPopular(count);
    }
}
