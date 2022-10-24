package ru.yandex.practicum.filmorate.exception;

public class DataAlreadyExistException extends RuntimeException {

    public DataAlreadyExistException(String s) {
        super(s);
    }
}
