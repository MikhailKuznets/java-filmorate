package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getLong("mpa_id"),
                rs.getString("mpa_name"));
    }

    @Override
    public Mpa get(Long id) {
        String sqlQuery = "select * from mpa where mpa_id = ?";
        final List<Mpa> films = jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa, id);
        if (films.size() != 1) {
            throw new DataNotFoundException("Не найден id = " + id);
        }
        return films.get(0);
    }

    @Override
    public List<Mpa> getAll(){
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa);
    }

}
