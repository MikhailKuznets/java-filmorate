package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Digits;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public abstract class StorageData {
    @Digits(integer = 12, fraction = 0)
    private Long id;
}
