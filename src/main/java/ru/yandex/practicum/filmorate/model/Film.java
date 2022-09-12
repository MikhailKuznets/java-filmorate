package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @Digits(integer = 12, fraction = 0)
    private int id;

    @NotBlank(message = "Необходимо указать название")
    private final String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private final String description;

    @ReleaseDate(message = "Некорректная дата релиза")
    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private final int duration;

}
