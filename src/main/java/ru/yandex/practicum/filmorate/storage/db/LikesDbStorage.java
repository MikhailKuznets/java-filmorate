package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    private void updateRate(Long filmId) {
        String sqlQuery = "update films f set rate = (select count(l.user_id) " +
                "from likes l where l.film_id=f.film_id) where f.film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "merge into likes (film_id, user_id) values (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = "select * from films f, mpa m where f.mpa_id = m.mpa_id order by rate limit ?";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, count);
    }
}
