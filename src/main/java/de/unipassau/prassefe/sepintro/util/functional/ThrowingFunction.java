package de.unipassau.prassefe.sepintro.util.functional;

/**
 * Functional interface for throwing functions.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T> The argument type.
 * @param <R> The return type.
 * @param <E> The exception type.
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    /**
     * Apply this function.
     * @param elem The element.
     * @return The return value.
     * @throws E The exception.
     */
    R apply(T elem) throws E;
}
