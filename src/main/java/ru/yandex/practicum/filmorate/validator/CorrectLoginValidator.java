package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectLoginValidator implements ConstraintValidator<CorrectLogin, String> {

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !(login.contains(" ") || login.isEmpty());
    }
}
