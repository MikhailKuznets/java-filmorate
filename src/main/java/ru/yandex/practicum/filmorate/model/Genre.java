package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre extends StorageData {
    private String name;

    public Genre(Integer id, String name) {
        this.id = (long) id.intValue();
        this.name = name;
    }
}
