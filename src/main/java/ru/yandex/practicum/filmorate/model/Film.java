package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Необходимо указать название")
    private final String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private final String description;

    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private final int duration;

}
