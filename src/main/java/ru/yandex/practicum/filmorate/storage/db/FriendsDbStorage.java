package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component("FriendsDbStorage")
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "merge into friends (user_id, friend_id) values(?,?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlQuery = "delete from friends where user_id=? and friend_id = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        String sqlQuery = "select * from users where user_id in (select friend_id from friends where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        String sqlQuery = "select * from users as u, friends as f, friends as o " +
                "where u.user_id = f.friend_id and u.user_id = o.friend_id " +
                "and f.user_id = ? and o.user_id = ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId, otherId);
    }
}
