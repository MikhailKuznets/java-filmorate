package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.CorrectLogin;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//@Data
//@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends StorageData {
    private String name;

//    @Digits(integer = 12, fraction = 0)
//    private Long id;

    @Email(message = "Некорректный Email")
    @NotBlank(message = "Некорректный Email")
    private String email;

    @CorrectLogin(message = "Логин не может быть пустым и содержать пробелы")
    private String login;

    @PastOrPresent(message = "День Рождения не может быть в будущем")
    private LocalDate birthday;

    @JsonIgnore
    private transient Set<Long> friendIds = new HashSet<>();

    public void addFriend(long friendId) {
        friendIds.add(friendId);
    }

    public void removeFriend(long friendId) {
        friendIds.remove(friendId);
    }

}
