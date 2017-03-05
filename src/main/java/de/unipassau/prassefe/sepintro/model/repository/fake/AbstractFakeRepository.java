package de.unipassau.prassefe.sepintro.model.repository.fake;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.CreateableRepository;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;

public abstract class AbstractFakeRepository<T, K> implements CreateableRepository<T, K> {
	protected abstract K getKey(T item);
	protected abstract void setKey(T item);
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
	public T getById(K id) {
		return getStorage().get(id);
	}

	protected T getByCondition(Predicate<T> predicate) {
		return getStorage().values().stream().filter(predicate).findAny().orElse(null);
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
	public void create() {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void setConfig(AbstractConfig config) {
		// do nothing
	}
	
	@Override
	public void deleteAll() {
		getStorage().clear();
	}
}
