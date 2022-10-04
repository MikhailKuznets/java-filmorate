package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService extends AbstractService<Film> {
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private static final int MAX_NAME_SIZE = 200;

    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate).reversed();

    private final Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @Override
    protected void validate(Film data) {
        if (data.getName() == null || data.getName().isBlank()) {
            throw new ValidationException("Film name invalid");
        }
        if (data.getDescription() != null && data.getDescription().length() > MAX_NAME_SIZE) {
            throw new ValidationException("Film description invalid");
        }
        if (data.getReleaseDate() == null || data.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            throw new ValidationException("Film release date invalid");
        }
        if (data.getDuration() <= 0) {
            throw new ValidationException("Film duration invalid");
        }
    }

    public void addLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.addLike(userId);
        log.info("Add like film id: {} from user: {}", id, userId);
    }

    public void removeLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.removeLike(userId);
        log.info("Remove like film id: {} from user: {}", id, userId);
    }

    public List<Film> getPopular(long count) {
        log.info("Get popular films count: {}", count);
        return storage.getAll().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }
}
