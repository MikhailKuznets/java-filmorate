package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.CorrectLogin;


import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private String name;

    @Digits(integer = 12, fraction = 0)
    private int id;

    @Email(message = "Некорректный Email")
    @NotBlank(message = "Некорректный Email")
    private final String email;

    @CorrectLogin(message = "Логин не может быть пустым и содержать пробелы")
    private final String login;

    @PastOrPresent(message = "День Рождения не может быть в будущем")
    private final LocalDate birthday;
}
