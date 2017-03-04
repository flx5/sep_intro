package de.unipassau.prassefe.sepintro.model.repository.fake;

@FunctionalInterface
public interface IdGenerator<T> {
	T next();
}
