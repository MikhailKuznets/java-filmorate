package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film extends StorageData{
//    @Digits(integer = 12, fraction = 0)
//    private Long id;

    @NotBlank(message = "Необходимо указать название")
    private final String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private final String description;

    @ReleaseDate(message = "Некорректная дата релиза")
    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private final int duration;

    @JsonIgnore
    private long rate = 0;

    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();

    public void addLike(long userId) {
        userIds.add(userId);
        rate = userIds.size();
    }

    public void removeLike(long userId) {
        userIds.add(userId);
        rate = userIds.size();
    }
}
