package fr.cyu.jee.util;

public interface ThrowingFunction<I, O, E extends Throwable> {

    O apply(I input) throws E;
}