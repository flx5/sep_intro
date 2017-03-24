package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Collection;
import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;

/**
 * General repository contract.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T> Type of the entity to be stored.
 * @param <K> Type of the entity key.
 */
public interface Repository<T, K> extends AutoCloseable {

    /**
     * Get all entries.
     *
     * @return All entries.
     */
    Collection<T> all();

    /**
     * Get entry by id.
     *
     * @param id The id.
     * @return The entry, if any.
     */
    Optional<T> getById(K id);

    /**
     * Update existing entry.
     *
     * @param value New data.
     */
    void update(T value);

    /**
     * Insert new entry.
     *
     * @param value New entry.
     */
    void insert(T value);

    /**
     * Delete based on identifier.
     *
     * @param id The id to delete.
     */
    void deleteById(K id);

    /**
     * Delete based on entry.
     *
     * @param value The entry to delete.
     */
    void delete(T value);

    /**
     * Delete everything. Use with caution!
     */
    void deleteAll();

    /**
     * {@inheritDoc}
     */
    @Override
    void close();

    /**
     * Set the currently used configuration.
     *
     * @param config The config.
     */
    void setConfig(AbstractConfig config);
}
