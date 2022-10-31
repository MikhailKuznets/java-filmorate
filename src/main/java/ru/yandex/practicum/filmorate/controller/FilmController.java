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
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Получен POST - запрос к /films, переданное значение FILM = {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        log.info("Получен PUT - запрос к /films, переданное значение FILM = {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен Get - запрос к /films");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        log.info("Получен Get - запрос к /films/{id}, id = {}", id);
        return filmService.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен Put - запрос к /films/{id}/like/{userId}, id = {}, userId = {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен Delete - запрос к /films/{id}/like/{userId}, id = {}, userId = {}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен Get - запрос к /films/popular , count = {}", count);
        return filmService.getPopular(count);
    }
}
