package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IdValidationException extends IllegalArgumentException{

    public IdValidationException(String s) {
        super(s);
    }
}
