package fr.cyu.jee.dto;

import fr.cyu.jee.util.ThrowingFunction;

import java.util.Optional;

@FunctionalInterface
public interface DTOConversion<I, O> {

    Optional<O> convert(I input);

    static <I, O> DTOConversion<I, O> fromThrowing(ThrowingFunction<I, O, Throwable> function) {
        return input -> {
            try {
                return Optional.ofNullable(function.apply(input));
            } catch (Throwable e) {
                return Optional.empty();
            }
        };
    }

    DTOConversion<String, Integer> STRING_TO_INT = fromThrowing(Integer::parseInt);
    DTOConversion<String, Double> STRING_TO_DOUBLE = fromThrowing(Double::parseDouble);
}
