package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;

@Data
@Builder
public abstract class StorageData {
    @Digits(integer = 12, fraction = 0)
    private Long id;
}
