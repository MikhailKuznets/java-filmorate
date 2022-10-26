package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Digits;

@Data
public abstract class StorageData {
    @Digits(integer = 12, fraction = 0)
    protected Long id;
}
