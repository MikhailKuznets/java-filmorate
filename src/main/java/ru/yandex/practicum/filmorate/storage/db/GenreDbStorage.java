package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    // ВЕБ
    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        );
    }

    // ВЕБ
    @Override
    public Genre get(Long id) {
        final String sqlQuery = "select * from genres where genre_id = ?";
        final List<Genre> films = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);
        if (films.size() != 1) {
            throw new DataNotFoundException("Не найден id = " + id);
        }
        return films.get(0);
    }

    // ВЕБ
    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    @Override
    public void load(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        final String sqlQuery = "select * from genres g, films_genre fg where fg.genre_id = g.genre_id and fg.film_id " +
                "in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getLong("FILM_ID"));
            film.addGenre(makeGenre(rs, 0));
        }, films.stream().map(StorageData::getId).toArray());

    }

}
