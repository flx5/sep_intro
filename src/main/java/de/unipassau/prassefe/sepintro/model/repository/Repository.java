package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Collection;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;

public interface Repository<T, K> extends AutoCloseable {
	Collection<T> all();
	T getById(K id);
	void update(T value);
	void insert(T value);
	void deleteById(K id);
	void delete(T value);

	@Override
	void close(); 
	
	void setConfig(AbstractConfig config);
}
