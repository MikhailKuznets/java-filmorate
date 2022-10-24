package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.CorrectLogin;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Builder
@Data
public class User extends StorageData {
    private String name;

    @Email(message = "Некорректный Email")
    @NotBlank(message = "Некорректный Email")
    private final String email;

    @CorrectLogin(message = "Логин не может быть пустым и содержать пробелы")
    private final String login;

    @PastOrPresent(message = "День Рождения не может быть в будущем")
    private final LocalDate birthday;

    @JsonIgnore
    private final Set<Long> friendIds = new HashSet<>();

    public void addFriend(long friendId) {
        friendIds.add(friendId);
    }

    public void removeFriend(long friendId) {
        friendIds.remove(friendId);
    }

    public List<Long> getFriendsIds() {
        return new ArrayList<>(friendIds);
    }

}
