package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor


public class Film extends StorageData {

    @NotBlank(message = "Необходимо указать название")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    @ReleaseDate(message = "Некорректная дата релиза")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    private Long rate = 0L;

    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();

    public void addLike(Long userId) {
        userIds.add(userId);
        rate++;
    }

    public void removeLike(Long userId) {
        userIds.remove(userId);
        rate--;
    }
}
