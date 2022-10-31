package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private static final int MAX_NAME_SIZE = 200;

    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate);

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;


    public Film create(Film film) {
        return filmStorage.create(film);
    }

    protected void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Film name invalid");
        }
        if (film.getDescription() != null && film.getDescription().length() > MAX_NAME_SIZE) {
            throw new ValidationException("Film description invalid");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            throw new ValidationException("Film release date invalid");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration invalid");
        }
        final Mpa mpa = film.getMpa();
        if (mpa == null) {
            throw new ValidationException("Film mpa invalid");
        }
    }

    public void addLike(long filmId, long userId) {
        final Film film = filmStorage.get(filmId);
        final User user = userStorage.get(userId);
        film.addLike(user);
        likesStorage.addLike(film.getId(), user.getId());
        log.info("Add like film id: {} from user: {}", filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        final Film film = filmStorage.get(filmId);
        final User user = userStorage.get(userId);
        likesStorage.removeLike(film.getId(), user.getId());
        film.removeLike(user);
        log.info("Remove like film id: {} from user: {}", filmId, userId);
    }

    public List<Film> getPopular(int count) {
        log.info("Get popular films count: {}", count);
        final List<Film> films = likesStorage.getPopular(count);
        return films;
    }

    public List<Film> getAll() {
        final List<Film> films = filmStorage.getAll();
        genreStorage.load(films);
        return films;
    }

    public Film update(Film data) {
        final Film film = filmStorage.get(data.getId());
        validate(data);
        data.setRate(film.getRate());
        return filmStorage.update(data);
    }

    public Film get(long id) {
        final Film film = filmStorage.get(id);
        genreStorage.load(Collections.singletonList(film));
        return film;
    }

}
