package ru.yandex.practicum.filmorate.exception;

public class IdValidationException extends IllegalArgumentException{

    public IdValidationException(String s) {
        super(s);
    }
}
