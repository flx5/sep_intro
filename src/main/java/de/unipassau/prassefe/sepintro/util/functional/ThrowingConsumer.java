package de.unipassau.prassefe.sepintro.util.functional;

/**
 * Functional interface for throwing consumers.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T> The accepted type.
 * @param <E> The exception type.
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

    /**
     * Accept an element.
     * @param elem The element.
     * @throws E The exception.
     */
    void accept(final T elem) throws E;
}
