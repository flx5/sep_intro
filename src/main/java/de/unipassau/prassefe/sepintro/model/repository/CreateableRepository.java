package de.unipassau.prassefe.sepintro.model.repository;

public interface CreateableRepository<T, K> extends Repository<T, K> {
	void create();
	void destroy();
}
