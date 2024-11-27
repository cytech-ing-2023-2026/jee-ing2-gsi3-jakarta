package fr.cyu.jee.dto;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public sealed interface DTOResult<T> {

    record Success<T>(T value) implements DTOResult<T> {}
    record DecodingFailure<T>() implements DTOResult<T> {}
    record ValidationFailure<T>(Set<ConstraintViolation<T>> violations) implements DTOResult<T> {}
}
