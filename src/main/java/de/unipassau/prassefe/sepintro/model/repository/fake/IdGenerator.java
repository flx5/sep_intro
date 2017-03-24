package de.unipassau.prassefe.sepintro.model.repository.fake;

/**
 * Generic identifier generator.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T> Type of identifier.
 */
@FunctionalInterface
public interface IdGenerator<T> {

    /**
     * Get next identifier.
     * @return Next id.
     */
    T next();
}
