package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("FilmDbStorage")
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;


    public Film create(Film film) {
        log.info("TEST");
        String sqlQuery = "insert into films (film_name, description, " +
                "release_date, duration, rate, mpa_id) values(?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        saveGenres(film);
        System.out.println(film);
        return film;
    }

    private void saveGenres(Film film) {
        final Long filmId = film.getId();
        jdbcTemplate.update("delete from films_genre where film_id = ?", filmId);
        final Set<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final ArrayList<Genre> genreList = new ArrayList<>(genres);
        genreList.sort(Comparator.comparing(Genre::getId));

                film.getGenres().clear();
        for (Genre genre : genreList) {
            film.addGenre(genre);
        }

        jdbcTemplate.batchUpdate(
                "insert into films_genre (film_id, genre_id) values (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genreList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genreList.size();
                    }
                });

    }

    @Override
    public Film get(Long id) {
        String sqlQuery = "select * from films f, mpa m where f.mpa_id = m.mpa_id and f.film_id = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        if (films.size() != 1) {
            throw new DataNotFoundException("Film id = " + id);
        }
        return films.get(0);
    }


    public Film update(Film film) {
        String sqlQuery = "update films set film_name = ?, description = ?, release_date = ?, " +
                "duration = ?, rate = ?, mpa_id = ? where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());

        jdbcTemplate.update("delete from films_genre where film_id = ?", film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("merge into films_genre(film_id,genre_id) values (?,?)", film.getId(), genre.getId());
                film.addGenre(genre);
            }
        }
        Film result = get(film.getId());
        for (Genre genre : getFilmGenres(film.getId())) {
            result.addGenre(genre);
        }
        return result;
    }

    private Collection<Genre> getFilmGenres(Long filmId) {
        String sqlQuery = "select * from genres where genre_id in (select genre_id from films_genre where film_id = ?)";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> GenreDbStorage.makeGenre(rs, rowNum), filmId);
    }

    static Film makeFilm(ResultSet rs, int rowNom) throws SQLException {
        return new Film(
                rs.getLong("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                rs.getLong("rate"),
                new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"))
        );
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * from films f, mpa m where f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
    }
}
