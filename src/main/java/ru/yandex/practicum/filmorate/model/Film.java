package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @NotNull
    private Mpa mpa;

    @JsonIgnore
    private final Set<User> userIds = new HashSet<>();

    private final Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration,
                long rate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }

    public void addLike(User user) {
        userIds.add(user);
        rate++;
    }

    public void removeLike(User user) {
        userIds.remove(user);
        rate--;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeGenre(Genre genre) {
       genres.remove(genre);
    }
}
