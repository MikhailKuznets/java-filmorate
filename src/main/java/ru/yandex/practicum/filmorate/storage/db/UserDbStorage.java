package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component("UserDbStorage")
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                Objects.requireNonNull(rs.getDate("birthday")).toLocalDate());
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users (email, login, user_name, birthday) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        log.debug("Создан user: {} присвоен id: {}", user.getName(), id);
        return user;
    }


    @Override
    public int update(User user) {
        String sqlQuery = "update users set email = ?, login = ?, user_name = ?, birthday = ?" +
                "where user_id = ?";
        return jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
    }

    @Override
    public User get(Long id) {
        String sqlQuery = "select * from users where user_id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
        if (users.size() != 1) {
            throw new DataNotFoundException("Не найден пользователь с id = " + id);
        }
        return users.get(0);
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    private Collection<User> getFriends(Long userId) {
        String sqlQuery = "select * from users where user_id in (select friend_id from friends where user_id = ?)";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> makeUser(rs, rowNum), userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        String sqlQuery = "select * from users where user_id = ? or user_id = ? " +
                "or user_id in (select friend_id from friends where user_id = ? or user_id = ? " +
                "group by friend_id having count(friend_id) > 1)";
        Collection<User> result = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> makeUser(rs, rowNum), userId, otherId, userId, otherId);
        return result;
    }
}
