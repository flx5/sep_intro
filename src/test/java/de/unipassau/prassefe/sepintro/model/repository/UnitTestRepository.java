package de.unipassau.prassefe.sepintro.model.repository;

public interface UnitTestRepository<T,K> extends CreateableRepository<T,K> {
	void deleteAll();
}
