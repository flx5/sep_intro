package de.unipassau.prassefe.sepintro.model.repository;

import de.unipassau.prassefe.sepintro.model.config.Config;

public interface Repository<T, K> extends AutoCloseable {
	T getById(K id);
	void update(T value);
	void insert(T value);
	void deleteById(K id);
	void delete(T value);
	
	void create();
	void destroy();
	
	@Override
	void close(); 
	
	void setConfig(Config config);
}
