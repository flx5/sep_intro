package de.unipassau.prassefe.sepintro.model.repository.fake;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.Repository;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;

/**
 * Base class for in memory repository implementation.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T> Type of the entity to be stored.
 * @param <K> Type of the entity key.
 */
public abstract class AbstractFakeRepository<T, K> implements Repository<T, K> {

    /**
     * Get the key of the given item.
     * @param item Item for which the key should be returned.
     * @return Key of the item.
     */
    protected abstract K getKey(T item);

    /**
     * Generate a key for item.
     * @param item The item for which a key should be set.
     */
    protected abstract void setKey(T item);

    /**
     * Get in memory storage.
     * @return The storage.
     */
    protected abstract ConcurrentMap<K, T> getStorage();

    @Override
    public Collection<T> all() {
        return getStorage().values();
    }

    @Override
    public void update(T value) {
        this.getStorage().put(getKey(value), value);
    }

    @Override
    public void insert(T value) {
        if (this.getStorage().containsKey(getKey(value))) {
            throw new RepositoryException("Value with key exists already!");
        }

        setKey(value);

        this.update(value);
    }

    @Override
    public Optional<T> getById(K id) {
        return Optional.ofNullable(getStorage().get(id));
    }

    /**
     * Get any entry that satisfies the predicate.
     * @param predicate The predicate to look for.
     * @return The found entry, if any.
     */
    protected Optional<T> getByCondition(Predicate<T> predicate) {
        return getStorage().values().stream().filter(predicate).findAny();
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void deleteById(K id) {
        getStorage().remove(id);
    }

    @Override
    public void delete(T value) {
        deleteById(getKey(value));
    }

    @Override
    public void deleteAll() {
        getStorage().clear();
    }

    @Override
    public void setConfig(AbstractConfig config) {
        // do nothing
    }
}
