package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private String name;

    @Digits(integer = 12, fraction = 0)
    private int id;

    @Email(message = "Некорректный Email")
    private final String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private final String login;

    @PastOrPresent(message = "День Рождения не может быть в будущем")
    private final LocalDate birthday;
}
