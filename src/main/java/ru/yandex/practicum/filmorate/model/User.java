package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.CorrectLogin;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends StorageData {
    private String name;

    @Email(message = "Некорректный Email")
    @NotBlank(message = "Некорректный Email")
    private String email;

    @CorrectLogin(message = "Логин не может быть пустым и содержать пробелы")
    private String login;

    @PastOrPresent(message = "День Рождения не может быть в будущем")
    private LocalDate birthday;

    @JsonIgnore
    private final Set<Long> friendIds = new HashSet<>();

    public User(long id, String email, String login, String user_name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = user_name;
        this.birthday = birthday;
    }

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
