package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GenreDbStorage implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");
        return new Genre(id, name);
    }

    @Override
    public Genre create(Genre data) {
        return null;
    }

    @Override
    public Genre update(Genre data) {
        return null;
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id).stream().findFirst();


    }

    @Override
    public Collection<Genre> getAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }
}
